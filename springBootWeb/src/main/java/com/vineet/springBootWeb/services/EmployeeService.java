package com.vineet.springBootWeb.services;

import com.vineet.springBootWeb.dto.EmployeeDTO;
import com.vineet.springBootWeb.entities.EmployeeEntity;
import com.vineet.springBootWeb.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<EmployeeDTO> findById(Long employeeId) {
        Optional<EmployeeEntity> employeeEntity =  employeeRepository.findById(employeeId);
        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class));
    }

    public List<EmployeeDTO> findAll() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        return employeeEntities.stream()
                .map(employeeEntity -> {
                    return modelMapper.map(employeeEntity, EmployeeDTO.class);
                }).collect(Collectors.toList());
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployee, EmployeeEntity.class);
        EmployeeEntity employeeEntity =  employeeRepository.save(toSaveEntity);
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeById(EmployeeDTO employeeDTO, Long employeeId) {
        System.out.println(employeeId);
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }

    public boolean isEmployeeDoesNotExists(Long employeeId) {
        return !employeeRepository.existsById(employeeId);
    }

    public boolean deleteEmployeeById(Long employeeId) {
        if(isEmployeeDoesNotExists(employeeId)) {
            return false;
        }
        employeeRepository.deleteById(employeeId);
        return true;
    }

    public EmployeeDTO updatePartialEmployeeById(Map<String, Object> updates, Long employeeId) {
        if(isEmployeeDoesNotExists(employeeId)) {
            return null;
        }
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();
        updates.forEach((key, value)->{
            Field toUpdate = ReflectionUtils.findRequiredField(EmployeeEntity.class, key);
            toUpdate.setAccessible(true);
            ReflectionUtils.setField(toUpdate, employeeEntity, value);
        });
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }
}
