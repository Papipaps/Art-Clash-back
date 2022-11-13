package com.example.demo.utils;

import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPageDTO<T> {

    List<T> content;

    CustomPage customPage;

    public CustomPageDTO(Page<T> page) {
        this.content = page.getContent();
        this.customPage = new CustomPage(page.getTotalElements(),
                page.getTotalPages(), page.getNumber(), page.getSize());
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public CustomPage getCustomPage() {
        return customPage;
    }

    public void setCustomPage(CustomPage customPage) {
        this.customPage = customPage;
    }
}