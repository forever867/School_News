<template>
	<view class="container">
		<view class="input-section">
			<text class="label">标题</text>
			<uni-easyinput v-model="post.title" placeholder="请输入帖子标题" maxlength="50"></uni-easyinput>
		</view>
		
		<view class="input-section">
			<text class="label">内容</text>
			<textarea v-model="post.content" placeholder="请输入帖子内容" maxlength="500" rows="10"></textarea>
		</view>
		
		<button class="submit-btn" @click="submitPost">发布帖子</button>
		
		<view class="loading" v-if="loading">
			<uni-loading type="spinner" color="#007AFF"></uni-loading>
			<text>正在发布...</text>
		</view>
	</view>
</template>

<script>
// 导入uniCloud模块
const db = uniCloud.database();

export default {
	data() {
		return {
			post: {
				title: '',
				content: '',
				schoolId: '',
				time: ''
			},
			loading: false
		}
	},
	onLoad() {
		const user = uni.getStorageSync('user');
		if (!user || user.type !== 'student') {
			uni.showToast({
				title: '请使用学生账号登录以发布帖子',
				icon: 'none'
			});
			uni.navigateTo({
				url: '/pages/login/login'
			});
			return;
		}
		this.post.schoolId = user.school.id;
	},
	methods: {
		submitPost() {
			if (!this.post.title) {
				uni.showToast({
					title: '请输入标题',
					icon: 'none'
				});
				return;
			}
			
			if (!this.post.content) {
				uni.showToast({
					title: '请输入内容',
					icon: 'none'
				});
				return;
			}
			
			this.loading = true;
			
			// 设置发布时间
			this.post.time = new Date().toLocaleString();
			
			// 存储帖子到云数据库
			db.collection('posts').add(this.post)
				.then(res => {
					this.loading = false;
					uni.showToast({
						title: '发布成功',
						icon: 'success'
					});
					
					// 同时存储到本地，以便快速显示
					let posts = uni.getStorageSync('posts') || [];
					posts.unshift(this.post);
					uni.setStorageSync('posts', posts);
					
					// 跳转到个人中心
					uni.navigateTo({
						url: '/pages/profile/profile'
					});
				})
				.catch(err => {
					this.loading = false;
					uni.showToast({
						title: '发布失败，请重试',
						icon: 'none'
					});
					console.error('发布帖子失败:', err);
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

.input-section {
	margin-bottom: 20px;
}

.label {
	display: block;
	font-size: 16px;
	font-weight: bold;
	color: #333;
	margin-bottom: 8px;
}

uni-easyinput {
	width: 100%;
}

textarea {
	width: 100%;
	padding: 10px;
	border: 1px solid #e0e0e0;
	border-radius: 8px;
	font-size: 14px;
	color: #333;
	background-color: white;
	resize: none;
}

.submit-btn {
	width: 100%;
	height: 45px;
	background-color: #007AFF;
	color: white;
	font-size: 16px;
	border-radius: 8px;
	margin-top: 20px;
}

.loading {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background-color: rgba(255,255,255,0.9);
	padding: 20px;
	border-radius: 10px;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.loading text {
	margin-top: 10px;
	color: #666;
	font-size: 14px;
}
</style>