package com.example.autowiredconstructor.controller;

import com.example.autowiredconstructor.service.BonusComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionalController {

    // required = false prevents the app from crashing when BonusComponent is missing
    @Autowired(required = false)
    private BonusComponent bonusComponent;

    @GetMapping("/bonus")
    public String checkBonus() {
        if (bonusComponent == null) {
            return "Task 4.5: The Bonus component is not available right now. (Handled gracefully)";
        }
        return bonusComponent.getBonus();
    }
}