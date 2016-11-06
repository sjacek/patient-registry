/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.grinno.patients.util;

/**
 *
 * @author jacek
 */
public class PeselValidator {

    private final byte PESEL[] = new byte[11];
    private boolean valid = false;

    public static boolean peselIsValid(String pesel) {
        PeselValidator validator = new PeselValidator(pesel);
        return validator.isValid();
    }
    
    public PeselValidator(String pesel) {
        if (pesel.length() != 11) {
            valid = false;
        } else {
            for (int i = 0; i < 11; i++) {
                PESEL[i] = Byte.parseByte(pesel.substring(i, i + 1));
            }
            valid = checkSum() && checkMonth() && checkDay();
        }
    }

    public boolean isValid() {
        return valid;
    }

    public int getBirthYear() {
        int year = 10 * PESEL[0] + PESEL[1];
        int month = 10 * PESEL[2] + PESEL[3];

        if (month > 80 && month < 93) {
            year += 1800;
        } else if (month > 0 && month < 13) {
            year += 1900;
        } else if (month > 20 && month < 33) {
            year += 2000;
        } else if (month > 40 && month < 53) {
            year += 2100;
        } else if (month > 60 && month < 73) {
            year += 2200;
        }
        return year;
    }

    public int getBirthMonth() {
        int month;
        month = 10 * PESEL[2];
        month += PESEL[3];
        if (month > 80 && month < 93) {
            month -= 80;
        } else if (month > 20 && month < 33) {
            month -= 20;
        } else if (month > 40 && month < 53) {
            month -= 40;
        } else if (month > 60 && month < 73) {
            month -= 60;
        }
        return month;
    }

    public int getBirthDay() {
        int day = 10 * PESEL[4] + PESEL[5];
        return day;
    }

    public String getSex() {
        if (valid) {
            if (PESEL[9] % 2 == 1) {
                return "Mezczyzna";
            } else {
                return "Kobieta";
            }
        } else {
            return "---";
        }
    }

    private boolean checkSum() {
        int sum = 1 * PESEL[0]
                + 3 * PESEL[1]
                + 7 * PESEL[2]
                + 9 * PESEL[3]
                + 1 * PESEL[4]
                + 3 * PESEL[5]
                + 7 * PESEL[6]
                + 9 * PESEL[7]
                + 1 * PESEL[8]
                + 3 * PESEL[9];
        sum %= 10;
        sum = 10 - sum;
        sum %= 10;

        return sum == PESEL[10];
    }

    private boolean checkMonth() {
        int month = getBirthMonth();
        int day = getBirthDay();

        return month > 0 && month < 13;
    }

    private boolean checkDay() {
        int year = getBirthYear();
        int month = getBirthMonth();
        int day = getBirthDay();
        
        if ((day > 0 && day < 32) && (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)) {
            return true;
        }

        if ((day > 0 && day < 31) && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return true;
        }

        return (day > 0 && day < 30 && leapYear(year)) || (day > 0 && day < 29 && !leapYear(year));
    }

    private boolean leapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }
}
