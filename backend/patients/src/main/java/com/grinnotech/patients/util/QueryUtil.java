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
package com.grinnotech.patients.util;

//import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
//import com.mongodb.client.FindIterable;
//import org.springframework.data.com.grinnotech.patients.domain.PageRequest;
//import org.springframework.data.com.grinnotech.patients.domain.Pageable;
//import org.springframework.data.com.grinnotech.patients.domain.Sort;
//import org.springframework.data.com.grinnotech.patients.domain.Sort.Order;
//
//import java.util.List;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//import static ch.ralscha.extdirectspring.bean.SortDirection.ASCENDING;
//import static java.util.stream.Collectors.toList;
//import static org.springframework.data.com.grinnotech.patients.domain.PageRequest.of;
//import static org.springframework.data.com.grinnotech.patients.domain.Sort.Direction.ASC;
//import static org.springframework.data.com.grinnotech.patients.domain.Sort.Direction.DESC;
//import static org.springframework.data.com.grinnotech.patients.domain.Sort.by;
//
///**
// *
// * @author Jacek Sztajnke
// */
//public class QueryUtil {
//
//    public static <T> Stream<T> stream(FindIterable<T> iterable) {
//        return StreamSupport.stream(iterable.spliterator(), false);
//    }
//
//    public static Sort getSpringSort(ExtDirectStoreReadRequest request) {
//        List<Order> list = request.getSorters().stream().map(sortInfo ->
//                new Order(sortInfo.getDirection() == ASCENDING ? ASC : DESC, sortInfo.getProperty())
//        ).collect(toList());
//        return by(list);
//    }
//
//    public static Pageable getPageable(ExtDirectStoreReadRequest request) {
//        return of(request.getPage() - 1, request.getLimit(), getSpringSort(request));
//    }
//}
