package org.example.storage.rest;

import org.example.storage.dto.FileDto;
import org.example.storage.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {

    private final FileService fileService;

    public FileRestControllerV1(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public List<FileDto> getAll() {
        return fileService.getAllFiles();
    }

    @GetMapping("{id}")
    public FileDto getFile(@PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @PostMapping
    public FileDto upload(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @PutMapping("{id}")
    public FileDto update(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return fileService.updateFile(id, file);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        fileService.deleteFileById(id);
    }
}
