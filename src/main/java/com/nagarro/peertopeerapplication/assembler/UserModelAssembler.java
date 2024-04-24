package com.nagarro.peertopeerapplication.assembler;

import com.nagarro.peertopeerapplication.controller.AdminController;
import com.nagarro.peertopeerapplication.controller.UserController;
import com.nagarro.peertopeerapplication.dto.UserDTO;
import com.nagarro.peertopeerapplication.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO user) {
        return EntityModel.of(user,
               // linkTo(methodOn(UserController.class).getSpecificUser(user.getId())).withSelfRel(),
                linkTo(methodOn(AdminController.class).getAllUsers()).withRel("users"));
    }
}
