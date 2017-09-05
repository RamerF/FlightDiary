package org.ramer.diary.service.impl;

import org.ramer.diary.domain.Albums;
import org.ramer.diary.repository.AlbumsRepository;
import org.ramer.diary.service.AlbumsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AlbumsServiceImpl implements AlbumsService{
    @Resource
    private AlbumsRepository albumsRepository;

    @Override
    public Albums save(Albums albums) {
        return albumsRepository.saveAndFlush(albums);
    }
}