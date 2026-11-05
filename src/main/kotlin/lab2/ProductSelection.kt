package lab2

fun selectProducts(
    products: List<Product>,
    recommendedIds: List<Long>,
    excludedIds: List<Long>
): List<Product> {
    val byId = HashMap<Long, Product>(products.size)
    for (product in products) {
        byId[product.id] = product
    }

    val excluded = excludedIds.toHashSet()
    val added = HashSet<Long>()
    val result = ArrayList<Product>()

    for (id in recommendedIds) {
        if (id in added || id in excluded) continue

        val product = byId[id] ?: continue
        if (!product.available) continue

        result.add(product)
        added.add(id)
    }

    return result
}
