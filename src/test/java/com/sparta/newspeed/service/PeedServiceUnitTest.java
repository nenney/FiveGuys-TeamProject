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
        // given
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "테스트 내용");
        User user = new User();
        user.setNickname("testuser");

        Peed peed = new Peed(requestDto, user);
        when(peedRepository.save(any(Peed.class))).thenReturn(peed);

        // when
        PeedResponseDto responseDto = peedService.createPeed(requestDto, new UserDetailsImpl(user));

        // then
        assertNotNull(responseDto);
        assertEquals("테스트 내용", responseDto.getContents());
        assertEquals("testuser", responseDto.getNickname());
    }

    @Test
    public void testUpdatePeed() {
        // given
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "업데이트된 내용");
        User user = new User();
        user.setNickname("testuser");

        Peed peed = new Peed(requestDto, user);
        when(peedRepository.findById(any(Long.class))).thenReturn(Optional.of(peed));
        when(peedRepository.save(any(Peed.class))).thenReturn(peed);

        // when
        PeedResponseDto responseDto = peedService.updatePeed(1L, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("업데이트된 내용", responseDto.getContents());
    }

    @Test
    public void testDeletePeed() {
        // given
        User user = new User();
        user.setNickname("testuser");

        Peed peed = new Peed(new PeedRequestDto("testuser", "삭제할 내용"), user);
        when(peedRepository.findById(any(Long.class))).thenReturn(Optional.of(peed));

        // when-then, assertDoesNotThrow을 사용하여 간소화함
        assertDoesNotThrow(() -> peedService.deletePeed(1L, new PeedRequestDto("testuser", "삭제할 내용")));
    }

    @Test
    public void testCreatePeedWithInvalidData() {
        // given
        PeedRequestDto requestDto = new PeedRequestDto("", ""); // 잘못된 데이터

        // when-then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            peedService.createPeed(requestDto, null);
        });

        String expectedMessage = "Invalid data";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}