package controller;

import dto.UtilisateurDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    // ...injection de service...

    @GetMapping
    public List<UtilisateurDTO> getAll() {
        // ...
        return null;
    }

    @PostMapping
    public UtilisateurDTO create(@RequestBody UtilisateurDTO dto) {
        // ...
        return null;
    }

    @GetMapping("/{id}")
    public UtilisateurDTO getById(@PathVariable Long id) {
        // ...
        return null;
    }

    @PutMapping("/{id}")
    public UtilisateurDTO update(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        // ...
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        // ...
    }
}

