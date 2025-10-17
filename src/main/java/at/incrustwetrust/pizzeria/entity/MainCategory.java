package at.incrustwetrust.pizzeria.entity;

import java.util.List;
import java.util.Map;

import static at.incrustwetrust.pizzeria.entity.SubCategory.*;

public enum MainCategory {
    STARTER,
    MAIN_COURSE,
    DESSERT,
    DRINK;



    private static final Map<MainCategory , List<SubCategory>> validSubCategories = Map.of(
            MainCategory.STARTER, List.of(),
            MainCategory.MAIN_COURSE, List.of(PIZZA,PASTA,BOWL),
            MainCategory.DESSERT, List.of(),
            MainCategory.DRINK, List.of(ALCOHOL_FREE, BEER, WINE, SPIRIT, COFFEE)
    );

    public static List<SubCategory> getValidSubCategories(MainCategory mainCat) {
        return validSubCategories.getOrDefault(mainCat, List.of());
    }

    public static boolean isValidSubCategories(MainCategory mainCat, SubCategory subCat) {
        return validSubCategories.getOrDefault(mainCat, List.of()).contains(subCat);
    }

}
