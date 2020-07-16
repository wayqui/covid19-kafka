package com.wayqui.covid19;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/com/wayqui/covid19/features",
        glue = "com.wayqui.covid19.steps")
public class CucumberAcceptanceTest {
}
