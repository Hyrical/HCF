package org.hyrical.hcf.utils.menus.fill

import org.hyrical.hcf.utils.menus.fill.impl.BorderFiller
import org.hyrical.hcf.utils.menus.fill.impl.FillFiller

enum class FillTemplate(val menuFiller: IMenuFiller? = null) {
    FILL(FillFiller()), BORDER(BorderFiller());

}