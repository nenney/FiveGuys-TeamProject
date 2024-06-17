package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.PeedRequestDto;
import com.sparta.newspeed.dto.PeedResponseDto;
import com.sparta.newspeed.entity.Peed;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.repository.PeedRepository;
import com.sparta.newspeed.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PeedServiceUnitTest {

    @Mock
    private PeedRepository peedRepository;

    @InjectMocks
    private PeedService peedService;

    @Test
    public void testCreatePeed() {
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "Test content");
        User user = new User();
        user.setNickname("testuser");

        Peed peed = new Peed(requestDto, user);
        when(peedRepository.save(any(Peed.class))).thenReturn(peed);

        PeedResponseDto responseDto = peedService.createPeed(requestDto, new UserDetailsImpl(user));

        assertNotNull(responseDto);
        assertEquals("Test content", responseDto.getContents());
        assertEquals("testuser", responseDto.getNickname());
    }

    @Test
    public void testUpdatePeed() {
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "Updated content");
        User user = new User();
        user.setNickname("testuser");

        Peed peed = new Peed(requestDto, user);
        when(peedRepository.findById(any(Long.class))).thenReturn(Optional.of(peed));
        when(peedRepository.save(any(Peed.class))).thenReturn(peed);

        PeedResponseDto responseDto = peedService.updatePeed(1L, requestDto);

        assertNotNull(responseDto);
        assertEquals("Updated content", responseDto.getContents());
    }

    @Test
    public void testDeletePeed() {
        User user = new User();
        user.setNickname("testuser");

        Peed peed = new Peed(new PeedRequestDto("testuser", "Test content"), user);
        when(peedRepository.findById(any(Long.class))).thenReturn(Optional.of(peed));

        assertDoesNotThrow(() -> peedService.deletePeed(1L, new PeedRequestDto("testuser", "Test content")));
    }

    @Test
    public void testCreatePeedWithInvalidData() {
        PeedRequestDto requestDto = new PeedRequestDto("", ""); // Invalid data

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            peedService.createPeed(requestDto, null);
        });

        String expectedMessage = "Invalid data";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}