package com.sb.solutions.web.navigation.mapper;

import java.util.ArrayList;
import java.util.List;

import com.sb.solutions.web.navigation.dto.MenuDto;

/**
 * @author : Rujan Maharjan on  1/4/2021
 **/
public class MenuCreatorCAD {

    private static final String FILE_ICON = "file-text-outline";
    private static final String APPROVED_ICON = "checkmark-square-outline";
    private static final String PENDING_ICON = "edit-2-outline";
    public static final String UNASSIGNED_APPROVED_LOAN = "Unassigned Approved Loan";
    public static final String OFFER_LETTER_PENDING = "Offer Letter Pending";
    public static final String OFFER_LETTER_APPROVED = "Offer Letter Approved";
    public static final String DISBURSEMENT_PENDING = "Disbursement Pending";
    public static final String DISBURSEMENT_APPROVED = "Disbursement Approved";
    public static final String LEGAL_PENDING = "Legal Review Pending";
    public static final String ALL_CAD_FILE = "All CAD Files";
    public static final String CREDIT_ADMINISTRATION = "Credit Administration";
    public static final String BOOK_OPEN_ICON = "book-open-outline";
    public static final String CREDIT_ADMINISTRATION_BASE_LINK = "/home/credit";

    public static MenuDto cadNav() {
        MenuDto menuDto = new MenuDto();
        menuDto.setTitle(CREDIT_ADMINISTRATION);
        menuDto.setIcon(BOOK_OPEN_ICON);
        menuDto.setChildren(subNav());
        return menuDto;

    }


    private static List<MenuDto> subNav() {
        List<MenuDto> menuDtoList = new ArrayList<>();

        MenuDto menuDto = new MenuDto();
        menuDto.setTitle(UNASSIGNED_APPROVED_LOAN);
        menuDto.setIcon(FILE_ICON);
        menuDto.setLink(CREDIT_ADMINISTRATION_BASE_LINK);
        menuDtoList.add(menuDto);

        menuDto = new MenuDto();
        menuDto.setTitle(OFFER_LETTER_PENDING);
        menuDto.setIcon(PENDING_ICON);
        menuDto.setLink("/home/credit/offer-pending");
        menuDtoList.add(menuDto);

        menuDto = new MenuDto();
        menuDto.setTitle(OFFER_LETTER_APPROVED);
        menuDto.setIcon(APPROVED_ICON);
        menuDto.setLink("/home/credit/offer-approved");
        menuDtoList.add(menuDto);

        menuDto = new MenuDto();
        menuDto.setTitle(LEGAL_PENDING);
        menuDto.setIcon(PENDING_ICON);
        menuDto.setLink("/home/credit/legal-pending");
        menuDtoList.add(menuDto);

        menuDto = new MenuDto();
        menuDto.setTitle(DISBURSEMENT_PENDING);
        menuDto.setIcon(PENDING_ICON);
        menuDto.setLink("/home/credit/disbursement-pending");
        menuDtoList.add(menuDto);

        menuDto = new MenuDto();
        menuDto.setTitle(DISBURSEMENT_APPROVED);
        menuDto.setIcon(APPROVED_ICON);
        menuDto.setLink("/home/credit/disbursement-approved");
        menuDtoList.add(menuDto);

        menuDto = new MenuDto();
        menuDto.setTitle(ALL_CAD_FILE);
        menuDto.setIcon(FILE_ICON);
        menuDto.setLink("/home/credit/cad-documents");
        menuDtoList.add(menuDto);

        return menuDtoList;

    }

}
