import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useContactStore = defineStore('contact', () => {
  // State
  const messages = ref([])
  const isLoading = ref(false)

  // Computed
  const unreadCount = computed(() => {
    return messages.value.filter((m) => !m.isRead).length
  })

  const repliedCount = computed(() => {
    return messages.value.filter((m) => m.reply).length
  })

  // Actions
  const fetchMessages = async () => {
    isLoading.value = true
    try {
      // Simulate API call - replace with actual backend endpoint
      await new Promise((resolve) => setTimeout(resolve, 800))

      // Load from localStorage or use fallback data
      const stored = localStorage.getItem('contactMessages')
      if (stored) {
        messages.value = JSON.parse(stored)
      } else {
        // Initial demo data
        messages.value = [
          {
            id: 'msg-001',
            name: 'Sarah Johnson',
            email: 'sarah.johnson@email.com',
            subject: 'order',
            message:
              "Hi, I placed an order #12345 yesterday but haven't received a confirmation email. Could you please check the status?",
            createdAt: new Date(Date.now() - 3600000).toISOString(), // 1 hour ago
            isRead: false,
            reply: null,
            repliedAt: null,
            userId: null,
          },
          {
            id: 'msg-002',
            name: 'Michael Chen',
            email: 'mike.chen@email.com',
            subject: 'product',
            message:
              'I\'m interested in the MacBook Pro 16". Does it come with AppleCare+ included or is that purchased separately?',
            createdAt: new Date(Date.now() - 7200000).toISOString(), // 2 hours ago
            isRead: false,
            reply: null,
            repliedAt: null,
            userId: null,
          },
          {
            id: 'msg-003',
            name: 'Emily Rodriguez',
            email: 'emily.r@email.com',
            subject: 'support',
            message:
              "My iPhone 15 Pro keeps freezing after the latest update. I've tried restarting but the issue persists. What should I do?",
            createdAt: new Date(Date.now() - 14400000).toISOString(), // 4 hours ago
            isRead: true,
            reply:
              'Hello Emily! We apologize for the inconvenience. Please try the following: 1) Back up your data, 2) Go to Settings > General > Transfer or Reset iPhone > Erase All Content and Settings, 3) Restore from your backup. If the issue continues, please visit an Apple Store for further assistance.',
            repliedAt: new Date(Date.now() - 10800000).toISOString(), // 3 hours ago
            userId: null,
          },
          {
            id: 'msg-004',
            name: 'David Kim',
            email: 'david.kim@email.com',
            subject: 'refund',
            message:
              "I purchased AirPods Pro last week but they don't fit comfortably. Can I get a refund or exchange for a different product?",
            createdAt: new Date(Date.now() - 21600000).toISOString(), // 6 hours ago
            isRead: true,
            reply:
              'Hi David! Yes, we offer a 14-day return policy for all products. You can return the AirPods Pro for a full refund or exchange. Please bring your receipt and the product in its original packaging to any Apple Store, or you can initiate a return through our website.',
            repliedAt: new Date(Date.now() - 18000000).toISOString(), // 5 hours ago
            userId: null,
          },
          {
            id: 'msg-005',
            name: 'Lisa Wang',
            email: 'lisa.wang@email.com',
            subject: 'feedback',
            message:
              'Just wanted to say thank you for the excellent customer service! My Mac Studio arrived perfectly packaged and works beautifully. The team was very helpful during the purchase process.',
            createdAt: new Date(Date.now() - 86400000).toISOString(), // 1 day ago
            isRead: true,
            reply:
              "Thank you so much for your kind words, Lisa! We're thrilled to hear that you're happy with your Mac Studio and our service. If you ever need anything else, please don't hesitate to reach out. Enjoy your new Mac!",
            repliedAt: new Date(Date.now() - 82800000).toISOString(), // ~23 hours ago
            userId: null,
          },
          {
            id: 'msg-006',
            name: 'James Wilson',
            email: 'james.w@email.com',
            subject: 'product',
            message:
              "What's the difference between the iPad Air and iPad Pro? I'm a graphic designer and need something for digital art.",
            createdAt: new Date(Date.now() - 172800000).toISOString(), // 2 days ago
            isRead: false,
            reply: null,
            repliedAt: null,
            userId: null,
          },
          {
            id: 'msg-007',
            name: 'Sophia Martinez',
            email: 'sophia.martinez@email.com',
            subject: 'other',
            message:
              "Do you have any student discounts available? I'm starting university next month and need a MacBook for my studies.",
            createdAt: new Date(Date.now() - 259200000).toISOString(), // 3 days ago
            isRead: true,
            reply: null,
            repliedAt: null,
            userId: null,
          },
        ]
        saveToLocalStorage()
      }
    } catch (error) {
      console.error('Failed to fetch contact messages:', error)
    } finally {
      isLoading.value = false
    }
  }

  const addContactMessage = async (messageData) => {
    try {
      // Generate unique ID
      const newMessage = {
        id: `msg-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
        name: messageData.name,
        email: messageData.email,
        subject: messageData.subject,
        message: messageData.message,
        createdAt: new Date().toISOString(),
        isRead: false,
        reply: null,
        repliedAt: null,
        userId: messageData.userId || null,
      }

      // Add to messages array
      messages.value.unshift(newMessage)

      // Save to localStorage
      saveToLocalStorage()

      // Simulate API call to backend
      await new Promise((resolve) => setTimeout(resolve, 500))

      return newMessage
    } catch (error) {
      console.error('Failed to add contact message:', error)
      throw error
    }
  }

  const markAsRead = (messageId) => {
    const message = messages.value.find((m) => m.id === messageId)
    if (message) {
      message.isRead = true
      saveToLocalStorage()
    }
  }

  const toggleRead = (messageId) => {
    const message = messages.value.find((m) => m.id === messageId)
    if (message) {
      message.isRead = !message.isRead
      saveToLocalStorage()
    }
  }

  const deleteMessage = (messageId) => {
    const index = messages.value.findIndex((m) => m.id === messageId)
    if (index !== -1) {
      messages.value.splice(index, 1)
      saveToLocalStorage()
    }
  }

  const replyToMessage = async (messageId, replyText) => {
    try {
      const message = messages.value.find((m) => m.id === messageId)
      if (!message) {
        throw new Error('Message not found')
      }

      // Update message with reply
      message.reply = replyText
      message.repliedAt = new Date().toISOString()
      message.isRead = true

      // Save to localStorage
      saveToLocalStorage()

      // Simulate API call to send email
      await new Promise((resolve) => setTimeout(resolve, 1000))

      // TODO: Implement actual email sending via backend API
      console.log('Reply sent to:', message.email)
      console.log('Reply text:', replyText)

      return message
    } catch (error) {
      console.error('Failed to reply to message:', error)
      throw error
    }
  }

  const saveToLocalStorage = () => {
    try {
      localStorage.setItem('contactMessages', JSON.stringify(messages.value))
    } catch (error) {
      console.error('Failed to save to localStorage:', error)
    }
  }

  // Initialize on store creation
  const init = async () => {
    await fetchMessages()
  }

  return {
    // State
    messages,
    isLoading,

    // Computed
    unreadCount,
    repliedCount,

    // Actions
    fetchMessages,
    addContactMessage,
    markAsRead,
    toggleRead,
    deleteMessage,
    replyToMessage,
    init,
  }
})
