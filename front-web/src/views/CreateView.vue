<template>
  <div v-if="userStore.isLoggedIn" class="create-view">
    <h1>创作</h1>
    <p>开始创作你的内容</p>
  </div>
  <div v-else class="empty-view"></div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<style scoped>
.create-view {
  padding: 24px;
}

.empty-view {
  min-height: 100%;
}

h1 {
  font-size: 28px;
  margin-bottom: 16px;
}

p {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
}
</style>
