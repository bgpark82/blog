package com.springboot.jpa.controller;

import com.springboot.jpa.domain.item.Book;
import com.springboot.jpa.domain.item.Item;
import com.springboot.jpa.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }

    /**
     * 상품 목록
     */
    @GetMapping(value = "/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems(); model.addAttribute("items", items);
        return "items/itemList";
    }

    /**
     * 상품 수정 폼
     */
    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model
            model) {
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm(); form.setId(item.getId()); form.setName(item.getName()); form.setPrice(item.getPrice()); form.setStockQuantity(item.getStockQuantity()); form.setAuthor(item.getAuthor()); form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }
    /**
     * 상품 수정
     */
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {
        /**
         * Book
         * - 준영속 엔티티
         * - JPA에서 더이상 관리하지 않는 엔ㅌ티
         * - Book은 새로운 객체지만 getId로 id를 가져오는 것을 보니
         * 데이터베이스를 이미갔다왔다고 판단할 수 있다 = 준영속 엔티티
         * - 간단히 말해 DB에 저장이 되었으니 id를 가질 수 있으므로
         * - 문제는 JPA가 관리를 안한다는 점!
         * - 내가 아무리 값을 바꿔치기 해도 자동으로 update 시켜주지 않는다
         * - 그럼 어떠헤 데이터 변경할 수 있을가?
         * 1. 준영속이지만 변경감지 기법을 활용
         * 2. merge 사용
         */
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);
        /**
         * 어설프게 controller에서 entity를 생성하지 말고
         * id와 변경할 데이터를 명확하게 service로 전달하는 것이 좋다
         */
        itemService.updateItem(form.getId(), form.getName(), form.getPrice());
        return "redirect:/items";
    }
}
