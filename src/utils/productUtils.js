// Product data and utilities
export const formatPrice = (price) => {
  if (typeof price === 'string') {
    return price
  }
  return `$${price.toLocaleString()}`
}

export const getNumericPrice = (priceString) => {
  return parseInt(priceString.replace(/[^0-9]/g, ''))
}

export const formatProductName = (name) => {
  return name.replace(/([a-z])([A-Z])/g, '$1 $2')
}

export const generateProductSlug = (name) => {
  return name
    .toLowerCase()
    .replace(/\s+/g, '-')
    .replace(/[^a-z0-9-]/g, '')
}

// Color utilities
export const colorMap = {
  black: '#1d1d1f',
  white: '#f5f5f7',
  blue: '#1e3a8a',
  purple: '#7c3aed',
  pink: '#ec4899',
  gold: '#d97706',
  silver: '#94a3b8',
  red: '#dc2626',
  green: '#059669',
}

export const getColorHex = (colorName) => {
  return colorMap[colorName.toLowerCase()] || colorName
}

// Sorting utilities
export const sortProducts = (products, sortBy) => {
  const sorted = [...products]

  switch (sortBy) {
    case 'price-low':
      return sorted.sort((a, b) => getNumericPrice(a.price) - getNumericPrice(b.price))
    case 'price-high':
      return sorted.sort((a, b) => getNumericPrice(b.price) - getNumericPrice(a.price))
    case 'newest':
      return sorted.sort((a, b) => b.id - a.id)
    case 'name':
    default:
      return sorted.sort((a, b) => a.name.localeCompare(b.name))
  }
}

// Filter utilities
export const filterProducts = (products, { searchQuery, category, priceRange }) => {
  let filtered = [...products]

  if (searchQuery) {
    const query = searchQuery.toLowerCase()
    filtered = filtered.filter(
      (product) =>
        product.name.toLowerCase().includes(query) ||
        product.description?.toLowerCase().includes(query) ||
        product.category?.toLowerCase().includes(query),
    )
  }

  if (category && category !== 'All') {
    filtered = filtered.filter((product) => product.category === category)
  }

  if (priceRange) {
    filtered = filtered.filter((product) => {
      const price = getNumericPrice(product.price)
      return price >= priceRange.min && price <= priceRange.max
    })
  }

  return filtered
}
