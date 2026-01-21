<template>
  <aside class="sidebar">
    <nav class="nav-menu">
      <div
        v-for="item in menuItems"
        :key="item.key"
        class="nav-item"
        :class="{ active: activeKey === item.key }"
        @click="handleMenuClick(item)"
      >
        <component :is="item.icon" class="nav-icon" />
        <span class="nav-text">{{ item.label }}</span>
      </div>
      <div class="nav-divider"></div>
      <div
        v-for="item in bottomMenuItems"
        :key="item.key"
        class="nav-item"
        :class="{ active: activeKey === item.key }"
        @click="handleMenuClick(item)"
      >
        <component :is="item.icon" class="nav-icon" />
        <span class="nav-text">{{ item.label }}</span>
      </div>
    </nav>
  </aside>
</template>

<script setup lang="ts">
import { ref, h } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 定义菜单项类型
interface MenuItem {
  key: string
  label: string
  icon: any
  path: string
}

// 图标组件
const HomeIcon = () => h('svg', {
  xmlns: 'http://www.w3.org/2000/svg',
  width: '20',
  height: '20',
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  'stroke-width': '2',
  'stroke-linecap': 'round',
  'stroke-linejoin': 'round'
}, [
  h('path', { d: 'm3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z' }),
  h('polyline', { points: '9 22 9 12 15 12 15 22' })
])

const HotIcon = () => h('svg', {
  xmlns: 'http://www.w3.org/2000/svg',
  width: '20',
  height: '20',
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  'stroke-width': '2',
  'stroke-linecap': 'round',
  'stroke-linejoin': 'round'
}, [
  h('polyline', { points: '23 6 13.5 15.5 8.5 10.5 1 18' }),
  h('polyline', { points: '17 6 23 6 23 12' })
])

const UserIcon = () => h('svg', {
  xmlns: 'http://www.w3.org/2000/svg',
  width: '20',
  height: '20',
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  'stroke-width': '2',
  'stroke-linecap': 'round',
  'stroke-linejoin': 'round'
}, [
  h('path', { d: 'M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2' }),
  h('circle', { cx: '12', cy: '7', r: '4' })
])

const HeartIcon = () => h('svg', {
  xmlns: 'http://www.w3.org/2000/svg',
  width: '20',
  height: '20',
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  'stroke-width': '2',
  'stroke-linecap': 'round',
  'stroke-linejoin': 'round'
}, [
  h('path', { d: 'M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z' })
])

const PlusIcon = () => h('svg', {
  xmlns: 'http://www.w3.org/2000/svg',
  width: '20',
  height: '20',
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  'stroke-width': '2',
  'stroke-linecap': 'round',
  'stroke-linejoin': 'round'
}, [
  h('line', { x1: '12', y1: '5', x2: '12', y2: '19' }),
  h('line', { x1: '5', y1: '12', x2: '19', y2: '12' })
])

// 菜单项配置
const menuItems: MenuItem[] = [
  { key: 'home', label: '推荐', icon: HomeIcon, path: '/' },
  { key: 'hot', label: '热门', icon: HotIcon, path: '/hot' }
]

const bottomMenuItems: MenuItem[] = [
  { key: 'following', label: '关注', icon: HeartIcon, path: '/following' },
  { key: 'profile', label: '我的', icon: UserIcon, path: '/profile' },
  { key: 'create', label: '创作', icon: PlusIcon, path: '/create' }
]

const activeKey = ref('hot')

const handleMenuClick = (item: MenuItem) => {
  activeKey.value = item.key
  router.push(item.path)
}
</script>

<style scoped>
.sidebar {
  width: 180px;
  height: calc(100vh - 64px);
  background: #161823;
  border-top: none;
  overflow-y: auto;
  position: fixed;
  top: 64px;
  left: 0;
}

.nav-menu {
  padding: 16px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.2s;
  color: rgba(255, 255, 255, 0.85);
  font-size: 15px;
  margin: 0 12px;
  border-radius: 8px;
  position: relative;
}

.nav-item:hover {
  background: #242634;
  color: #ffffff;
}

.nav-item.active {
  background: #242634;
  color: #1890ff;
  font-weight: 500;
  box-shadow: 0 1px 4px rgba(255, 255, 255, 0.05);
}

.nav-icon {
  flex-shrink: 0;
  position: absolute;
  left: 50%;
  transform: translateX(calc(-50% - 40px));
}

.nav-text {
  flex: 1;
  text-align: center;
}

.nav-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.08);
  margin: 8px 16px;
}

/* 滚动条样式 */
.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-thumb {
  background: #3a3a4a;
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover {
  background: #4a4a5a;
}
</style>
