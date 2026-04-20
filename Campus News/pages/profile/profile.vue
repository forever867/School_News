<template>
	<view class="container">
		<view class="user-info" v-if="user">
			<view class="avatar">
				<uni-icons type="person" size="60" color="#007AFF"></uni-icons>
			</view>
			<text class="school-name" v-if="user.type === 'student'">
				{{ school.name }}
			</text>
			<text class="school-name" v-else>
				游客
			</text>
			<text class="user-role">
				{{ user.type === 'student' ? '学生' : '游客' }}
			</text>
			<text class="student-id" v-if="user.type === 'student'">
				学号: {{ user.studentId }}
			</text>
		</view>
		
		<view class="posts-section" v-if="user.type === 'student'">
			<view class="section-header">
				<text class="section-title">我的帖子</text>
				<text class="post-count">{{ posts.length }} 篇</text>
			</view>
			
			<view class="post-list">
				<view class="post-item" v-for="(post, index) in posts" :key="index">
					<text class="post-title">{{ post.title }}</text>
					<text class="post-time">{{ post.time }}</text>
					<text class="post-content">{{ post.content }}</text>
				</view>
				
				<view class="empty-posts" v-if="posts.length === 0">
					<uni-icons type="info" size="48" color="#999"></uni-icons>
					<text>还没有发布帖子</text>
					<button class="post-btn" @click="goToPost">发布第一个帖子</button>
				</view>
			</view>
		</view>
		
		<view class="guest-tip" v-else>
			<uni-icons type="info" size="48" color="#999"></uni-icons>
			<text>游客模式下只能浏览新闻，无法发帖</text>
			<text>请使用学生账号登录以使用完整功能</text>
		</view>
		
		<button class="logout-btn" @click="logout">退出登录</button>
	</view>
</template>

<script>
// 导入uniCloud模块
const db = uniCloud.database();

export default {
	data() {
		return {
			user: null,
			school: null,
			posts: [],
			loading: false
		}
	},
	onLoad() {
			const user = uni.getStorageSync('user');
			if (!user) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				uni.navigateTo({
					url: '/pages/login/login'
				});
				return;
			}
			
			this.user = user;
			if (user.type === 'student') {
				this.school = user.school;
				// 获取用户帖子
				this.getPosts();
			}
		},
	methods: {
		getPosts() {
			this.loading = true;
			
			// 从云数据库获取帖子
			db.collection('posts')
				.where({ schoolId: this.school.id })
				.orderBy('time', 'desc')
				.get()
				.then(res => {
					this.loading = false;
					if (res.code === 0) {
						this.posts = res.result.data;
						// 同时更新本地存储
						uni.setStorageSync('posts', this.posts);
					} else {
						// 从本地存储获取备份数据
						this.posts = uni.getStorageSync('posts') || [];
						this.posts = this.posts.filter(post => post.schoolId === this.school.id);
					}
				})
				.catch(err => {
					this.loading = false;
					// 从本地存储获取备份数据
					this.posts = uni.getStorageSync('posts') || [];
					this.posts = this.posts.filter(post => post.schoolId === this.school.id);
					console.error('获取帖子失败:', err);
				});
		},
		goToPost() {
			uni.navigateTo({
				url: '/pages/post/post'
			});
		},
		logout() {
				uni.showModal({
					title: '退出登录',
					content: '确定要退出登录吗？',
					confirmText: '确定',
					cancelText: '取消',
					success: (res) => {
						if (res.confirm) {
							// 清除登录信息
							uni.removeStorageSync('user');
							// 跳转到登录页面
							uni.navigateTo({
								url: '/pages/login/login'
							});
						}
					}
				});
			}
	}
}
</script>

<style>
.container {
	padding: 20px;
	min-height: 100vh;
}

.user-info {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 30px;
	padding: 20px;
	background-color: white;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.avatar {
	margin-bottom: 15px;
}

.school-name {
	font-size: 18px;
	font-weight: bold;
	color: #333;
	margin-bottom: 5px;
}

.user-role {
	font-size: 14px;
	color: #666;
}

.posts-section {
	background-color: white;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.05);
	padding: 20px;
	margin-bottom: 30px;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
	padding-bottom: 10px;
	border-bottom: 1px solid #f0f0f0;
}

.section-title {
	font-size: 16px;
	font-weight: bold;
	color: #333;
}

.post-count {
	font-size: 14px;
	color: #666;
}

.post-list {
	min-height: 200px;
}

.post-item {
	padding: 15px;
	border-bottom: 1px solid #f0f0f0;
	margin-bottom: 10px;
}

.post-title {
	display: block;
	font-size: 16px;
	font-weight: bold;
	color: #333;
	margin-bottom: 5px;
}

.post-time {
	display: block;
	font-size: 12px;
	color: #999;
	margin-bottom: 10px;
}

.post-content {
	display: block;
	font-size: 14px;
	color: #666;
	line-height: 1.5;
}

.empty-posts {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 40px 0;
}

.empty-posts text {
	margin: 15px 0;
	font-size: 14px;
	color: #999;
}

.post-btn {
	margin-top: 10px;
	padding: 8px 20px;
	background-color: #007AFF;
	color: white;
	font-size: 14px;
	border-radius: 20px;
}

.student-id {
	display: block;
	font-size: 14px;
	color: #666;
	margin-top: 5px;
}

.guest-tip {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 40px 20px;
	background-color: white;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.05);
	margin-bottom: 30px;
}

.guest-tip text {
	margin: 10px 0;
	font-size: 14px;
	color: #666;
	text-align: center;
}

.logout-btn {
	width: 100%;
	height: 45px;
	background-color: #f5f5f5;
	color: #333;
	font-size: 16px;
	border-radius: 8px;
}
</style>