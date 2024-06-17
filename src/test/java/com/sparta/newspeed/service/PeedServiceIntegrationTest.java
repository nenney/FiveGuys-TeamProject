package com.sparta.newspeed.service;

import com.sparta.newspeed.dto.PeedRequestDto;
import com.sparta.newspeed.dto.PeedResponseDto;
import com.sparta.newspeed.entity.Peed;
import com.sparta.newspeed.entity.User;
import com.sparta.newspeed.repository.PeedRepository;
import com.sparta.newspeed.repository.UserRepository;
import com.sparta.newspeed.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class PeedServiceIntegrationTest {

    @Autowired
    private PeedService peedService;

    @Autowired
    private PeedRepository peedRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setNickname("testuser");
        userRepository.save(testUser);
    }

    @Test
    public void testCreatePeed() {
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "Test content");
        PeedResponseDto responseDto = peedService.createPeed(requestDto, new UserDetailsImpl(testUser));

        assertNotNull(responseDto);
        assertEquals("Test content", responseDto.getContents());

        Optional<Peed> savedPeed = peedRepository.findById(responseDto.getId());
        assertTrue(savedPeed.isPresent());
        assertEquals("Test content", savedPeed.get().getContents());
    }

    @Test
    public void testGetAllPeeds() {
        PeedRequestDto requestDto = new PeedRequestDto("testuser", "Test content");
        peedService.createPeed(requestDto, new UserDetailsImpl(testUser));

        var allPeeds = peedService.getAllPeeds(0, 10, "id", true);
        assertFalse(allPeeds.isEmpty());
        assertEquals(1, allPeeds.getTotalElements());
    }
}