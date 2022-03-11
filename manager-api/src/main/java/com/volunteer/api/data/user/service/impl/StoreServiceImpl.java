package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.dto.Store;
import com.volunteer.api.data.user.repository.AddressRepository;
import com.volunteer.api.data.user.repository.StoreRepository;
import com.volunteer.api.data.user.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;

    @Override
    public Collection<Store> getAll() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Store> getById(Integer id) {
        return storeRepository.findById(id);
    }

    @Override
    public Collection<Store> getByName(String name) {
        return storeRepository.findAllByNameLike(StringUtils.defaultString(name) + "%");
    }

    @Override
    public Store create(Store store) {
        store.setId(null);
        updateAddresses(store);
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

        updateAddresses(current);

        return storeRepository.save(current);
    }

    private void updateAddresses(Store store) {
        store.setAddress(addressRepository.getById(store.getAddress().getId()));
        if (store.getRecipientAddress() != null) {
            store.setRecipientAddress(addressRepository.getById(store.getRecipientAddress().getId()));
        }
    }
}
