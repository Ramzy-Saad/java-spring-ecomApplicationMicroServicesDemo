package com.ecomMicroservices.user.services;

import com.ecomMicroservices.user.dto.AddressDto;
import com.ecomMicroservices.user.dto.UserRequest;
import com.ecomMicroservices.user.dto.UserResponse;
import com.ecomMicroservices.user.models.Address;
import com.ecomMicroservices.user.models.User;
import com.ecomMicroservices.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUSer(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUSer(UserRequest userRequest){
        User user = new User();
        mapUserRequestToUser(user, userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(Long id){
        return userRepository.findById(id).map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest){
        return userRepository.findById(id)
                .map(existingUser -> {
                    mapUserRequestToUser(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        if(user.getAddress() != null){
            AddressDto addressDto = new AddressDto();
            addressDto.setCity(user.getAddress().getCity());
            addressDto.setCountry(user.getAddress().getCountry());
            addressDto.setState(user.getAddress().getState());
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDto);
        }
        return response;
    }


    private void mapUserRequestToUser(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null){
            Address address = new Address();
            address.setCountry(userRequest.getAddress().getCountry());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setCity(userRequest.getAddress().getCity());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }
}
