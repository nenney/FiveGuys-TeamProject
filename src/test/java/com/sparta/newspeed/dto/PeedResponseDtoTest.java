package com.sparta.newspeed.dto;

import com.sparta.newspeed.entity.Peed;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PeedResponseDtoTest {

    @Test
    public void testPeedResponseDto() {
        Peed peed = new Peed();
        ReflectionTestUtils.setField(peed, "id", 1L);
        ReflectionTestUtils.setField(peed, "nickname", "testuser");
        ReflectionTestUtils.setField(peed, "contents", "Test content");
        ReflectionTestUtils.setField(peed, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(peed, "modifiedAt", LocalDateTime.now());

        PeedResponseDto responseDto = new PeedResponseDto(peed);

        assertEquals(1L, responseDto.getId());
        assertEquals("testuser", responseDto.getNickname());
        assertEquals("Test content", responseDto.getContents());
        assertNotNull(responseDto.getCreatedAt());
        assertNotNull(responseDto.getModifiedAt());
    }

    @Test
    public void testPeedResponseDtoWithLikeCount() {
        Peed peed = new Peed();
        peed.setLikesCount(5);

        PeedResponseDto responseDto = new PeedResponseDto(peed);

        assertEquals(5, responseDto.getLikeCount());
    }
}