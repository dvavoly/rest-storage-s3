package org.example.storage.service;

import org.example.storage.config.StorageConfig;
import org.example.storage.dto.FileDto;
import org.example.storage.exception.FileNotFoundInStorageException;
import org.example.storage.exception.UserNotFoundByEmailException;
import org.example.storage.model.File;
import org.example.storage.model.User;
import org.example.storage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final UserService userService;
    private final StorageConfig storage;
    @Value("${application.bucket.name}")
    private String bucket;

    public FileService(FileRepository fileRepository, StorageConfig storage, UserService userService) {
        this.fileRepository = fileRepository;
        this.storage = storage;
        this.userService = userService;
    }

    public List<FileDto> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(file -> new FileDto(file.getId(), file.getFileName()))
                .toList();
    }

    public FileDto getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .map(file -> new FileDto(file.getId(), file.getFileName()))
                .orElseThrow(() -> new FileNotFoundInStorageException(fileId));
    }

    public FileDto uploadFile(MultipartFile file) {
        var user = getUserFromSecurityContext();
        persistFileToS3Storage(user, file);
        var result = saveFileInformationToDB(user, file);
        return new FileDto(result.getId(), result.getFileName());
    }

    public FileDto updateFile(Long fileId, MultipartFile file) {
        var result = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundInStorageException(fileId));
        result.setFileName(file.getOriginalFilename());
        var savedFile = fileRepository.save(result);

        var key = getS3ObjectKey(result.getUser().getEmail(), result.getFileName());
        removeFileFromS3Storage(key); //TODO object doesn't remove
        persistFileToS3Storage(savedFile.getUser(), file);
        return new FileDto(savedFile.getId(), savedFile.getFileName());
    }

    public void deleteFileById(Long fileId) {
        var file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundInStorageException(fileId));
        var key = getS3ObjectKey(file.getUser().getEmail(), file.getFileName());
        removeFileFromS3Storage(key);

        fileRepository.deleteById(fileId);
    }

    private User getUserFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userEmail = authentication.getName();

        return userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundByEmailException(userEmail));
    }

    private void persistFileToS3Storage(User user, MultipartFile file) {
        var key = getS3ObjectKey(user, file);
        var objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try (var s3 = storage.s3()) {
            s3.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeFileFromS3Storage(String key) {
        var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        try (var s3 = storage.s3()) {
            s3.deleteObject(deleteObjectRequest);
        }
    }

    private String getS3ObjectKey(User user, MultipartFile file) {
        return user.getEmail() + "_" + file.getOriginalFilename();
    }

    private String getS3ObjectKey(String email, String fileName) {
        return email + "_" + fileName;
    }

    private File saveFileInformationToDB(User user, MultipartFile file) {
        var result = File.builder()
                .fileName(file.getOriginalFilename())
                .user(user)
                .location(file.getOriginalFilename()) // TODO Make a presigned URL to share an object
                .build();
        return fileRepository.save(result);
    }
}
