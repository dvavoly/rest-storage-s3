package org.example.storage.rest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.storage.model.Event;
import org.example.storage.model.User;
import org.example.storage.model.enums.Role;
import org.example.storage.model.enums.Status;
import org.example.storage.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerV1Test {

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository mockRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "First name", "Last name",
                "user@user.com", "p@$$w0rd", Role.ADMIN, Status.ACTIVE,
                List.of(new Event(1L, LocalDateTime.now(), 1L, 1L),
                        new Event(2L, LocalDateTime.now(), 1L, 2L)));
    }

    @DisplayName("Test for getUserById method")
    @Test
    void givenUserId_whenGetUserById_thenOk() throws Exception {
        when(mockRepository.findById(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/v1/users/1")
                        .with(user("admin").password("admin").authorities(Role.ADMIN.getAuthority())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is("First name")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Last name")))
                .andExpect(jsonPath("$.email", Matchers.is("user@user.com")))
                .andExpect(jsonPath("$.role", Matchers.is(Role.ADMIN.name())))
                .andExpect(jsonPath("$.status", Matchers.is(Status.ACTIVE.name())));

        verify(mockRepository, times(1)).findById(1L);
    }

    @DisplayName("Test for getUserById method with UserNotFound response")
    @Test
    void givenUserId_whenGetUserById_whenUserNotFound404() throws Exception {
        mockMvc.perform(get("/api/v1/users/9999")
                        .with(user("admin").password("admin").authorities(Role.ADMIN.getAuthority())))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test for getAll method")
    @Test
    void givenUsersList_whenGetAll_thenReturnUsersList() throws Exception {
        var secondUser = new User(2L, "First SecondUser name", "Last SecondUser name",
                "user2@user.com", "p@$$w0rd", Role.MODERATOR, Status.ACTIVE,

                List.of(new Event(1L, LocalDateTime.now(), 2L, 1L),
                        new Event(2L, LocalDateTime.now(), 2L, 2L)));
        List<User> users = List.of(user, secondUser);

        when(mockRepository.findAll()).thenReturn(users);
        mockMvc.perform(get("/api/v1/users/")
                        .with(user("admin").password("admin").authorities(Role.ADMIN.getAuthority())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].firstName", Matchers.is("First name")))
                .andExpect(jsonPath("$[0].lastName", Matchers.is("Last name")))
                .andExpect(jsonPath("$[0].email", Matchers.is("user@user.com")))
                .andExpect(jsonPath("$[0].role", Matchers.is(Role.ADMIN.name())))
                .andExpect(jsonPath("$[0].status", Matchers.is(Status.ACTIVE.name())))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].firstName", Matchers.is("First SecondUser name")))
                .andExpect(jsonPath("$[1].lastName", Matchers.is("Last SecondUser name")))
                .andExpect(jsonPath("$[1].email", Matchers.is("user2@user.com")))
                .andExpect(jsonPath("$[1].role", Matchers.is(Role.MODERATOR.name())))
                .andExpect(jsonPath("$[1].status", Matchers.is(Status.ACTIVE.name())));

        verify(mockRepository, times(1)).findAll();
    }

    @DisplayName("Test for createUser method")
    @Test
    void givenNewUser_whenCreateUser_whenReturnUser() throws Exception {
        var mapper = mapperBuilder.build();
        mapper.registerModule(new JavaTimeModule());

        when(mockRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/users")
                        .with(user("admin").password("admin").authorities(Role.ADMIN.getAuthority()))
                        .content(mapper.writeValueAsString(user))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is("First name")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Last name")))
                .andExpect(jsonPath("$.email", Matchers.is("user@user.com")))
                .andExpect(jsonPath("$.role", Matchers.is(Role.ADMIN.name())))
                .andExpect(jsonPath("$.status", Matchers.is(Status.ACTIVE.name())));

        verify(mockRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Test for update User method")
    @Test
    void givenUser_whenUpdateUser_thenResponseOk() throws Exception {
        var mapper = mapperBuilder.build();
        mapper.registerModule(new JavaTimeModule());

        when(mockRepository.save(any(User.class))).thenReturn(user);
        when(mockRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        mockMvc.perform(put("/api/v1/users/1").with(user("admin").password("admin").authorities(Role.ADMIN.getAuthority()))
                        .content(mapper.writeValueAsString(user))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is("First name")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Last name")))
                .andExpect(jsonPath("$.email", Matchers.is("user@user.com")))
                .andExpect(jsonPath("$.role", Matchers.is(Role.ADMIN.name())))
                .andExpect(jsonPath("$.status", Matchers.is(Status.ACTIVE.name())));

        verify(mockRepository, times(1)).save(any(User.class));
    }

    @DisplayName("Test for Delete user method")
    @Test
    void givenUserId_whenDeleteUser_whenNoContent() throws Exception {
        doNothing().when(mockRepository).deleteById(1L);
        mockMvc.perform(delete("/api/v1/users/1")
                        .with(user("admin").password("admin").authorities(Role.ADMIN.getAuthority())))
                .andExpect(status().isNoContent());
        verify(mockRepository, times(1)).deleteById(1L);
    }
}
