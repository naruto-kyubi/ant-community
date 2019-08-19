package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.article.domain.UserTag;
import org.naruto.framework.article.repository.TagRepository;
import org.naruto.framework.article.repository.UserTagRepository;
import org.naruto.framework.core.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TagServiceImpl implements TagService{

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserTagRepository userTagRepository;


    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public List<Tag> queryTags() {
        return tagRepository.findAll();
    }

    @Override
    public UserTag save(UserTag userTag) {
        return userTagRepository.save(userTag);
    }

    @Override
    public Page<Tag> queryByPage(Map map) {
        return tagRepository.queryPageByCondition(map);
    }

    @Override
    public Page<Map> queryUserTags(String userId, String currentUserId, Map map) {
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return userTagRepository.queryUserTags(userId,currentUserId,pageable);
    }

    @Override
    public Page<Tag> queryUserTags(String userId, Map map) {
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return userTagRepository.queryUserTags(userId,pageable);
    }

    @Override
    public Page<Map> queryTags(String userId, Map map) {
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return userTagRepository.queryTags(userId,pageable);
    }

    @Override
    public void deleteUserTags(String userId, String tagId) {
        userTagRepository.deleteUserTagsByUserIdAndTagId(userId,tagId);
    }
}
