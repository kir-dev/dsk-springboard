package hu.bme.dsk.users

import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/api/users")
class UserController (
    private val userService: UserService,
) {
    @PostMapping("/authsch")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody profile: AuthschProfileResponse): DetailedUserDto {
        return userService.generateUser(profile)
    }

    @PostMapping("/google")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody profile: GoogleUserInfoResponse): DetailedUserDto {
        return userService.generateUser(profile)
    }

    @GetMapping("/authsch/{authId}")
    @ResponseStatus(HttpStatus.OK)
    fun findByAuthId(@PathVariable authId: String): DetailedUserDto {
        return userService.findByAuthId(authId)
    }

    @GetMapping("/google/{googleId}")
    @ResponseStatus(HttpStatus.OK)
    fun findByGoogleId(@PathVariable googleId: String): DetailedUserDto {
        return userService.findByGoogleId(googleId)
    }

    @GetMapping("/internal/{internalId}")
    @ResponseStatus(HttpStatus.OK)
    fun findByInternalId(@PathVariable internalId: String): DetailedUserDto {
        return userService.findByInternalId(internalId)
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun findByUserId(@PathVariable userId: UUID): DetailedUserDto {
        return userService.getById(userId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): List<UserDto> {
        return userService.getAllUsers()
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateById(@PathVariable userId: UUID, @Valid @RequestBody dto: UpdateUserDto) : DetailedUserDto {
        return userService.updateUser(userId, dto)
    }

    @PatchMapping("/internal/{internalId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateByInternalId(@PathVariable internalId: String, @Valid @RequestBody dto: UpdateUserDto) : DetailedUserDto {
        val user = userService.findByInternalId(internalId)
        return userService.updateUser(user.id, dto)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable userId: UUID) {
        userService.deleteUser(userId)
    }

    @DeleteMapping("/internal/{internalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable internalId: String) {
        userService.deleteUserByInternalId(internalId)
    }

}