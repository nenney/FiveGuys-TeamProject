package com.sparta.newspeed.entity;

import com.sparta.newspeed.dto.PeedRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PeedTest {

    @Test
    public void testPeedCreationWithRequestDto() {
        PeedRequestDto requestDto = new PeedRequestDto("TestUser", "Test content");

        Peed peed = new Peed(requestDto);

        assertEquals("Test content", peed.getContents());
    }

    @Test
    public void testPeedCreationWithRequestDtoAndUser() {
        User user = new User();
        user.setNickname("testuser");
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "Test content");

        Peed peed = new Peed(requestDto, user);

        assertEquals("testuser", peed.getNickname());
        assertEquals("Test content", peed.getContents());
        assertEquals(user, peed.getUser());
        assertTrue(user.getPeedlist().contains(peed));
    }

    @Test
    public void testUpdatePeed() {
        PeedRequestDto requestDto = new PeedRequestDto("TestUser", "Updated content");

        Peed peed = new Peed();
        peed.update(requestDto);

        assertEquals("Updated content", peed.getContents());
    }

    @Test
    public void testLikesCountIncrement() {
        Peed peed = new Peed();
        peed.likesCount("+");

        assertEquals(1, peed.getLikesCount());
    }

    @Test
    public void testLikesCountDecrement() {
        Peed peed = new Peed();
        peed.likesCount("+");
        peed.likesCount("-");

        assertEquals(0, peed.getLikesCount());
    }

    @Test
    public void testLikesCountInvalidOperation() {
        Peed peed = new Peed();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            peed.likesCount("invalid");
        });

        String expectedMessage = "Invalid operation";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}