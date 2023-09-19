package com.adrian.bookstoreapi.users.controller;

import com.adrian.bookstoreapi.common.constants.PaginationConstants;
import com.adrian.bookstoreapi.common.constants.RoleConstants;
import com.adrian.bookstoreapi.users.dto.PaginatedUsersResponseDto;
import com.adrian.bookstoreapi.users.dto.UserRequestDto;
import com.adrian.bookstoreapi.users.dto.UserResponseDto;
import com.adrian.bookstoreapi.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;


    @GetMapping
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<PaginatedUsersResponseDto> findAll(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIR) String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("{id}")
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<UserResponseDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(userService.findOne(id), UserResponseDto.class));
    }

    @PatchMapping("{id}")
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.update(id, userRequestDto));
    }

}
