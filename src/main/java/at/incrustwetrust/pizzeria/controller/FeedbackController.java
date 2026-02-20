package at.incrustwetrust.pizzeria.controller;

import at.incrustwetrust.pizzeria.entity.Feedback;
import at.incrustwetrust.pizzeria.repository.FeedbackRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Die MongoDB-Spielwiese für Kundenrückmeldungen")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    @PostMapping
    @Operation(summary = "Neues Feedback speichern", description = "Akzeptiert jedes JSON im 'details' Feld.")
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @GetMapping
    @Operation(summary = "Alle Feedbacks abrufen")
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}