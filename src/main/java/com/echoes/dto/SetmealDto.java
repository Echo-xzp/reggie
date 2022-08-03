package com.echoes.dto;


import com.echoes.entity.Setmeal;
import com.echoes.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
