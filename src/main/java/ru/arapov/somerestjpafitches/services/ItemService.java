package ru.arapov.somerestjpafitches.services;

import org.springframework.stereotype.Service;
import ru.arapov.somerestjpafitches.models.Item;
import ru.arapov.somerestjpafitches.repos.ItemRepository;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long itemId) {
        return itemRepository.findItemById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public String deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
        return "item - '" + itemId + "' removed!";
    }
}
