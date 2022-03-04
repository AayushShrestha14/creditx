package com.sb.solutions.web.navigation.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sb.solutions.api.authorization.entity.RolePermissionRights;
import com.sb.solutions.api.authorization.entity.SubNav;
import com.sb.solutions.web.navigation.dto.MenuDto;

/**
 * @author Rujan Maharjan on 5/17/2019
 */
@Component
public class MenuMapper {


    public List<MenuDto> menuDtoList(List<RolePermissionRights> rolePermissionRightsList) {
        final List<MenuDto> menuDtoList = new ArrayList<>();
        for (final RolePermissionRights rolePermissionRights : rolePermissionRightsList) {
            menuDtoList.add(menuDto(rolePermissionRights));
        }

        return menuDtoList;
    }

    public MenuDto menuDto(RolePermissionRights rolePermissionRights) {
        MenuDto m = new MenuDto();
        m.setTitle(rolePermissionRights.getPermission().getPermissionName());
        m.setLink(rolePermissionRights.getPermission().getFrontUrl());
        m.setIcon(rolePermissionRights.getPermission().getFaIcon());

        final List<MenuDto> subNavList = new ArrayList<>();

        for (SubNav subNav1 : rolePermissionRights.getPermission().getSubNavs()) {
            MenuDto subNav = new MenuDto();
            subNav.setTitle(subNav1.getSubNavName());
            subNav.setLink(subNav1.getFrontUrl());
            subNav.setIcon(subNav1.getFaIcon());
            subNavList.add(subNav);
        }

        if (CollectionUtils.isNotEmpty(subNavList)) {
            m.setChildren(subNavList);
        }

        return m;
    }
}
