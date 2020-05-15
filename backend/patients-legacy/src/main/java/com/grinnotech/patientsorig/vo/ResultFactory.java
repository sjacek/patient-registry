/*
 * Copyright (C) 2016 jacek
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
package com.grinnotech.patientsorig.vo;

/**
 *
 * @author Jacek Sztajnke
 */
public class ResultFactory {
    public static <T> Result<T> getSuccessResult(T data) {
        return new Result<>(true, data);
    }

    public static <T> Result<T> getSuccessResult(T data, String msg) {
        return new Result<>(true, msg);
    }
    
    public static <T> Result<T> getSuccessResultMsg(String msg) {
        return new Result<>(true, msg);
    }

    public static <T> Result<T> getFailResult(String msg) {
        return new Result<>(false, msg);
    }
}
