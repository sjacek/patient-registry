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
package com.grinnotech.patientsorig.service;

import com.grinnotech.patients.model.Foo;
import com.grinnotech.patientsorig.vo.Result;
import com.grinnotech.patientsorig.vo.ResultFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author Jacek Sztajnke
 */
@Transactional
@Service("fooRestService")
public class FooRestServiceImpl implements FooRestService {

    @Override
    public Result<Foo> update(String idFoo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<Foo> destroy(String idFoo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<Foo> read(String idFoo) {
        Foo foo = new Foo();
        return ResultFactory.getSuccessResult(foo);
    }

    @Override
    public Result<List<Foo>> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
