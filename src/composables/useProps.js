import { ref, computed, watch } from 'vue'

/**
 * Composable for managing product display preferences
 * @param {Object} initialSettings - Initial display settings
 * @returns {Object} - Reactive display settings and methods
 */
export function useProductDisplay(initialSettings = {}) {
  // Reactive state for display settings
  const settings = ref({
    columns: initialSettings.columns || 3,
    cardSize: initialSettings.cardSize || 'medium',
    theme: initialSettings.theme || 'light',
    showColors: initialSettings.showColors ?? true,
    showDescriptions: initialSettings.showDescriptions ?? true,
    showBadges: initialSettings.showBadges ?? true,
    compact: initialSettings.compact ?? false,
    ...initialSettings,
  })

  // Computed properties for derived state
  const gridClass = computed(() => `grid-${settings.value.columns}`)
  const isCompact = computed(() => settings.value.compact)
  const isDarkTheme = computed(() => settings.value.theme === 'dark')

  // Methods to update settings
  const updateSetting = (key, value) => {
    if (key in settings.value) {
      settings.value[key] = value
    }
  }

  const toggleCompact = () => {
    settings.value.compact = !settings.value.compact
  }

  const toggleTheme = () => {
    settings.value.theme = settings.value.theme === 'light' ? 'dark' : 'light'
  }

  const setColumns = (count) => {
    if (count >= 1 && count <= 6) {
      settings.value.columns = count
    }
  }

  const resetToDefaults = () => {
    settings.value = {
      columns: 3,
      cardSize: 'medium',
      theme: 'light',
      showColors: true,
      showDescriptions: true,
      showBadges: true,
      compact: false,
      ...initialSettings,
    }
  }

  // Save to localStorage if available
  const saveSettings = () => {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('productDisplaySettings', JSON.stringify(settings.value))
    }
  }

  const loadSettings = () => {
    if (typeof localStorage !== 'undefined') {
      const saved = localStorage.getItem('productDisplaySettings')
      if (saved) {
        try {
          const parsed = JSON.parse(saved)
          Object.assign(settings.value, parsed)
        } catch (e) {
          console.warn('Failed to parse saved settings:', e)
        }
      }
    }
  }

  // Watch for changes and auto-save
  watch(settings, saveSettings, { deep: true })

  return {
    settings,
    gridClass,
    isCompact,
    isDarkTheme,
    updateSetting,
    toggleCompact,
    toggleTheme,
    setColumns,
    resetToDefaults,
    saveSettings,
    loadSettings,
  }
}

/**
 * Composable for managing component props validation and defaults
 * @param {Object} propDefinitions - Vue prop definitions
 * @param {Object} receivedProps - Props received by component
 * @returns {Object} - Validated props and utilities
 */
export function usePropsValidation(propDefinitions, receivedProps) {
  const validatedProps = ref({})
  const validationErrors = ref([])

  // Validate props against definitions
  const validateProps = () => {
    validationErrors.value = []
    const validated = {}

    Object.keys(propDefinitions).forEach((propName) => {
      const definition = propDefinitions[propName]
      const receivedValue = receivedProps[propName]

      // Use default if no value provided
      if (receivedValue === undefined) {
        if (definition.default !== undefined) {
          validated[propName] =
            typeof definition.default === 'function' ? definition.default() : definition.default
        } else if (definition.required) {
          validationErrors.value.push(`Required prop '${propName}' is missing`)
        }
        return
      }

      // Type validation
      if (definition.type) {
        const types = Array.isArray(definition.type) ? definition.type : [definition.type]
        const isValid = types.some((type) => {
          if (type === String) return typeof receivedValue === 'string'
          if (type === Number) return typeof receivedValue === 'number'
          if (type === Boolean) return typeof receivedValue === 'boolean'
          if (type === Array) return Array.isArray(receivedValue)
          if (type === Object)
            return typeof receivedValue === 'object' && !Array.isArray(receivedValue)
          if (type === Function) return typeof receivedValue === 'function'
          return receivedValue instanceof type
        })

        if (!isValid) {
          validationErrors.value.push(`Prop '${propName}' has invalid type`)
          return
        }
      }

      // Custom validator
      if (definition.validator && !definition.validator(receivedValue)) {
        validationErrors.value.push(`Prop '${propName}' failed custom validation`)
        return
      }

      validated[propName] = receivedValue
    })

    validatedProps.value = validated
  }

  // Initial validation
  validateProps()

  // Watch for prop changes
  watch(() => receivedProps, validateProps, { deep: true })

  const hasErrors = computed(() => validationErrors.value.length > 0)
  const isValid = computed(() => !hasErrors.value)

  return {
    validatedProps,
    validationErrors,
    hasErrors,
    isValid,
    validateProps,
  }
}

/**
 * Composable for creating prop-based configurations
 * @param {Object} props - Component props
 * @param {Object} defaults - Default configuration
 * @returns {Object} - Merged configuration
 */
export function usePropsConfig(props, defaults = {}) {
  const config = computed(() => ({
    ...defaults,
    ...props,
    // Special handling for object props
    style: {
      ...defaults.style,
      ...props.style,
    },
    class: [defaults.class, props.class].filter(Boolean).join(' '),
  }))

  return {
    config,
  }
}
