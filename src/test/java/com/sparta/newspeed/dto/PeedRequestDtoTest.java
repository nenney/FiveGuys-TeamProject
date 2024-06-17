package com.sparta.newspeed.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PeedRequestDtoTest {

    @Test
    public void testPeedRequestDto() {
        PeedRequestDto requestDto = new PeedRequestDto("TestUser", "Test content");

        assertEquals("TestUser", requestDto.getUsername());
        assertEquals("Test content", requestDto.getContents());
    }

    @Test
    public void testPeedRequestDtoSettersAndGetters() {
        PeedRequestDto requestDto = new PeedRequestDto("TestUser", "Test content");

        assertEquals("TestUser", requestDto.getUsername());
        assertEquals("Test content", requestDto.getContents());
    }
}