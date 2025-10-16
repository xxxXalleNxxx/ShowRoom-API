package ru.arapov.somerestjpafitches.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arapov.somerestjpafitches.models.Item;
import ru.arapov.somerestjpafitches.services.ItemService;

import java.util.List;

@RestController
@RequestMapping("api/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/create")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        Item newItem = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @GetMapping()
    public ResponseEntity<List<Item>> getItems() {
        return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.getItemById(itemId), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItemById(@PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.deleteItem(itemId), HttpStatus.OK);
    }
}
