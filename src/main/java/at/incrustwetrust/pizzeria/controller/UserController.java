package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.dto.UserCreateDTO;
import at.incrustwetrust.pizzeria.dto.UserUpdateDTO;
import at.incrustwetrust.pizzeria.entity.User;
import at.incrustwetrust.pizzeria.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping ("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){ this.userService = userService;}

    @GetMapping
    public List<User> readAll ()
    {
        return this.userService.readAll();
    }

    @GetMapping("/{id}")
    public User read (@PathVariable String id)
    {
        return this.userService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create (@RequestBody  UserCreateDTO dto)
    {
        return this.userService.create(dto,null);
    }

    @PutMapping("/{id}")
    @ResponseStatus (HttpStatus.OK)
    public User update (@RequestBody @Valid UserUpdateDTO user, @PathVariable String id)
    {
        return this.userService.update(user, id);
    }

    @DeleteMapping ("/{id}")
    @ResponseStatus (HttpStatus.OK)
    public User delete (@PathVariable String id)
    {
        return this.userService.delete(id);
    }

}
