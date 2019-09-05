package com.petpool.application.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;

import lombok.Data;

@Data
public class Age implements Comparator {

    private int years;

    private int months;

    private int days;

    private Age(int years, int months, int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }

    public static Age createByDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(birthDate.getYear(), birthDate.getMonth(), birthDate.getDayOfMonth());

        Period p = Period.between(birthday, today);

        return new Age(p.getYears(), p.getMonths(), p.getDays());
    }

    public static Age createByYearAndMonthAndDay(int years, int months, int days) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(years, months, days);

        Period p = Period.between(birthday, today);

        return new Age(p.getYears(), p.getMonths(), p.getDays());
    }

    @Override
    public int compare(Object o1, Object o2) {
        Age age1 = (Age) o1;
        Age age2 = (Age) o2;

        return (age1.getYears() + age1.getMonths() + age1.getDays()) < (age2.getYears() + age2.getMonths() + age2.getDays()) ? -1 : age1 == age2 ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age = (Age) o;
        return years == age.years &&
                months == age.months;
    }

}
