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
package com.grinno.patients.ajax;

import com.grinno.patients.dao.UserRepository;
import com.grinno.patients.model.User;
import com.grinno.patients.vo.Result;
import com.grinno.patients.vo.ResultFactory;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jacek
 */
@Controller
@RequestMapping("/user")
public class UserHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    public UserRepository userRepository;
    
    @RequestMapping(value="/find", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String find(@RequestParam(value = "id", required = true) String id, HttpServletRequest request) {
        LOGGER.debug("UserHandler.user()");
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null)
            return getJsonErrorMsg("User is not logged on");
*/        
        User user = userRepository.findOneById(id);
        Result<User> result = ResultFactory.getSuccessResult(user);
        return getJsonSuccessData(result.getData());
//        return getJsonErrorMsg(ar.getMsg());
    }

    @RequestMapping(value="/store", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public String store(@RequestParam(value="data", required = true) String jsonData, HttpServletRequest request) {
        LOGGER.debug("UserHandler.store(" + jsonData + ")");
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null)
            return getJsonErrorMsg("User is not logged on");
*/
        JsonObject jsonObj = parseJsonObject(jsonData);

        List<String> authorities = new ArrayList<>();
        jsonObj.getJsonArray("authorities").stream().forEach(v -> authorities.add(v.toString()) );
        
        User user = new User(
                jsonObj.getString("loginName"),
                jsonObj.getString("firstName"),
                jsonObj.getString("lastName"),
                jsonObj.getString("email"),
                authorities,
                jsonObj.getString("passwordHash")
                );
        userRepository.insert(user);
        Result<User> result = ResultFactory.getSuccessResult(user);
        return getJsonSuccessData(result.getData());
    }

    @RequestMapping(value="/findAll", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String findAll(HttpServletRequest request) {
        LOGGER.debug("UserHandler.findAll()");
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            return getJsonErrorMsg("User is not logged on");
        }
*/
        Result<List<User>> result = ResultFactory.getSuccessResult(userRepository.findAll());
        
        if (result.isSuccess())
            return getJsonSuccessData(result.getData());
        return getJsonErrorMsg(result.getMsg());
    }

    @RequestMapping(value="/remove", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public String remove(@RequestParam(value = "data", required = true) String jsonData, HttpServletRequest request) {
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            return getJsonErrorMsg("User is not logged on");
        }
*/
        JsonObject jsonObj = parseJsonObject(jsonData);
        Result<User> result = ResultFactory.getSuccessResult(userRepository.remove(jsonObj.getString("id")));

        if (result.isSuccess())
            return getJsonSuccessData(result.getData());
        return getJsonErrorMsg(result.getMsg());
    }
}
