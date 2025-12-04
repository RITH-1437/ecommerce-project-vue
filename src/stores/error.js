import { defineStore } from 'pinia'

function toError(e) {
  if (e instanceof Error) return e
  if (typeof e === 'string') return new Error(e)
  try {
    return new Error(JSON.stringify(e))
  } catch (_) {
    return new Error('Unknown error')
  }
}

export const useErrorStore = defineStore('error', {
  state: () => ({
    latest: null, // { message, error, info, meta, time }
    queue: [],
    showBanner: false,
  }),
  getters: {
    message: (s) => (s.latest?.message ? s.latest.message : ''),
  },
  actions: {
    capture(err, info = '', meta = {}) {
      const error = toError(err)
      const payload = {
        message: error.message,
        error,
        info,
        meta,
        time: new Date().toISOString(),
      }
      this.latest = payload
      this.queue.push(payload)
      this.showBanner = true
      // eslint-disable-next-line no-console
      console.error('[Global Error]', info, error)
    },
    clearBanner() {
      this.showBanner = false
    },
    reset() {
      this.latest = null
      this.queue = []
      this.showBanner = false
    },
  },
})
