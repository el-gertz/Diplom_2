package praktikum.dto;

import java.util.List;

public class Order {

    public List<String> getIngredients() {
        return ingredients;
    }

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> ingredients;

}
