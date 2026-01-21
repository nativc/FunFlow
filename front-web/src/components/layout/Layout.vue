<template>
  <div class="layout">
    <Header />
    <div class="layout-container">
      <Sidebar />
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import Header from './Header.vue'
import Sidebar from './Sidebar.vue'
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-container {
  display: flex;
  flex: 1;
}

.main-content {
  margin-left: 180px;
  margin-top: 64px;
  flex: 1;
  height: calc(100vh - 64px);
  overflow-y: auto;
}

/* 滚动条样式 */
.main-content::-webkit-scrollbar {
  width: 8px;
}

.main-content::-webkit-scrollbar-thumb {
  background: #3a3a4a;
  border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb:hover {
  background: #4a4a5a;
}
</style>
