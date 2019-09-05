package com.petpool.domain.model.pet;

import java.time.LocalDate;

import com.petpool.application.util.Age;
import org.junit.Test;
import org.testng.Assert;

public class AgeTest {
    @Test
    public void getValidAge() {
        LocalDate birthDate = LocalDate.of(2018, 8, 5);

        Assert.assertEquals(Age.createByYearAndMonthAndDay(2018, 8, 5), Age.createByDate(birthDate));
    }

}