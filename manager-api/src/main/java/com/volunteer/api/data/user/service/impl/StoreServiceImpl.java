package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.dto.Store;
import com.volunteer.api.data.user.repository.StoreRepository;
import com.volunteer.api.data.user.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Override
    public Collection<Store> getAll() {
        return storeRepository.findAll();
    }

    @Override
    public Store getById(Integer id) {
        return storeRepository.getById(id);
    }

    @Override
    public Collection<Store> getByName(String name) {
        return storeRepository.findAllByNameLike(StringUtils.defaultString(name) + "%");
    }

    @Override
    public Store create(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store update(Store store) {
        Store current = storeRepository.getById(store.getId());

        current.setAddress(store.getAddress());
        current.setRecipientAddress(store.getRecipientAddress());
        current.setName(store.getName());
        current.setNote(store.getNote());
        current.setContactPerson(store.getContactPerson());

        return storeRepository.save(current);
    }
}
