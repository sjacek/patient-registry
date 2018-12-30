/*
 * Copyright (C) 2016 Jacek Sztajnke
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
package com.grinnotech.patients.vo;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Jacek Sztajnke
 * @param <T>
 */
public class Result<T> implements Serializable {

    final private boolean success;
    final private T data;
    final private String msg;

    Result(boolean success, T data) {
        this.success = success;
        this.data = data;
        this.msg = null;
    }

    Result(boolean success, String msg) {
        this.success = success;
        this.data = null;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("\"Result\"")
                .append("success=").append(success)
                .append(", msg=").append(msg)
                .append(", data=");

        if (data == null) {
            sb.append("null");
        }
        else if (data instanceof List) {
            List castList = (List)data;
            if (castList.isEmpty())
                sb.append("empty list");
            else {
                Object firstItem = castList.get(0);
                sb.append("List of").append(firstItem.getClass());
            }
        } else {
            sb.append(data.toString());
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.success ? 1 : 0);
        hash = 89 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Result<?> other = (Result<?>) obj;
        if (this.success != other.success) {
            return false;
        }
        return Objects.deepEquals(this.data, other.data);
    }
}
