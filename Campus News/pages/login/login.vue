<template>
	<view class="container">
		<view class="logo">
			<image src="../../static/logo.png" mode="aspectFit"></image>
		</view>
		<view class="title">校园新闻系统</view>
		
		<view class="login-mode">
			<view class="mode-item" :class="{ active: loginMode === 'guest' }" @click="loginMode = 'guest'">
				<text>游客登录</text>
			</view>
			<view class="mode-item" :class="{ active: loginMode === 'student' }" @click="loginMode = 'student'">
				<text>学生登录</text>
			</view>
		</view>
		
		<view class="school-list" v-if="loginMode === 'student'">
			<view class="school-item" v-for="school in schools" :key="school.id" @click="selectSchool(school)" :class="{ selected: selectedSchool && selectedSchool.id === school.id }">
				<text class="school-name">{{ school.name }}</text>
			</view>
		</view>
		
		<view class="student-login" v-if="loginMode === 'student' && selectedSchool">
			<uni-easyinput v-model="studentId" placeholder="请输入学号" maxlength="20"></uni-easyinput>
			<uni-easyinput v-model="password" type="password" placeholder="请输入密码" maxlength="20"></uni-easyinput>
		</view>
		
		<button class="login-btn" @click="login" :disabled="(loginMode === 'student' && !selectedSchool) || (loginMode === 'student' && (!studentId || !password))">
			{{ loginMode === 'guest' ? '游客登录' : '学生登录' }}
		</button>
		
		<view class="loading" v-if="loading">
			<uni-loading type="spinner" color="#007AFF"></uni-loading>
			<text>{{ loadingText }}</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			loginMode: 'guest', // 'guest' or 'student'
			schools: [
				{ id: 1, name: '北京大学', domain: 'pku.edu.cn' },
				{ id: 2, name: '清华大学', domain: 'tsinghua.edu.cn' },
				{ id: 3, name: '复旦大学', domain: 'fudan.edu.cn' },
				{ id: 4, name: '上海交通大学', domain: 'sjtu.edu.cn' },
				{ id: 5, name: '浙江大学', domain: 'zju.edu.cn' },
				{ id: 6, name: '青岛科技大学', domain: 'qust.edu.cn', vpnUrl: 'https://wvpn.qust.edu.cn/http/77726476706e69737468656265737421f9e7408f347e79456d1cc7a99c406d3609/tpass/login?service=https%3A%2F%2Fwvpn.qust.edu.cn%2Flogin%3Fcas_login%3Dtrue' },
				{ id: 7, name: '中国海洋大学', domain: 'ouc.edu.cn', vpnUrl: 'https://vpn.ouc.edu.cn/' },
				{ id: 8, name: '青岛大学', domain: 'qdu.edu.cn', vpnUrl: 'https://vpn.qdu.edu.cn/' },
				{ id: 9, name: '青岛理工大学', domain: 'qut.edu.cn', vpnUrl: 'https://vpn.qut.edu.cn/' },
				{ id: 10, name: '青岛农业大学', domain: 'qau.edu.cn', vpnUrl: 'https://vpn.qau.edu.cn/' }
			],
			selectedSchool: null,
			studentId: '',
			password: '',
			loading: false,
			loadingText: '正在处理登录...'
		}
	},
	methods: {
		selectSchool(school) {
			this.selectedSchool = school;
		},
		login() {
			if (this.loginMode === 'student') {
				if (!this.selectedSchool) {
					uni.showToast({
						title: '请选择学校',
						icon: 'none'
					});
					return;
				}
				
				if (!this.studentId || !this.password) {
					uni.showToast({
						title: '请输入学号和密码',
						icon: 'none'
					});
					return;
				}
				
				this.loading = true;
				this.loadingText = '正在连接校园官网...';
				
				// 连接到所选学校的教务系统进行登录验证
				this.loadingText = '正在连接' + this.selectedSchool.name + '教务系统...';
				
				setTimeout(() => {
					this.loadingText = '正在验证账号密码...';
					
					setTimeout(() => {
						// 模拟教务系统验证过程
						// 实际应用中，这里会通过API调用连接到学校的教务系统进行认证
						this.loading = false;
						
						// 假设验证成功（实际应用中需要根据教务系统返回结果判断）
						uni.showToast({
							title: '登录成功',
							icon: 'success'
						});
						// 存储登录信息
						uni.setStorageSync('user', {
							type: 'student',
							school: this.selectedSchool,
							studentId: this.studentId
						});
						// 跳转到首页
						uni.switchTab({
							url: '/pages/index/index'
						});
					}, 1500);
				}, 1500);
			} else {
				// 游客登录
				this.loading = true;
				this.loadingText = '正在登录...';
				
				setTimeout(() => {
					this.loading = false;
					uni.showToast({
						title: '游客登录成功',
						icon: 'success'
					});
					// 存储登录信息
					uni.setStorageSync('user', {
						type: 'guest'
					});
					// 跳转到首页
					uni.switchTab({
						url: '/pages/index/index'
					});
				}, 1000);
			}
		}
	}
}
</script>

<style>
.container {
	padding: 20px;
	display: flex;
	flex-direction: column;
	align-items: center;
	min-height: 100vh;
}

.logo {
	margin-top: 60px;
	margin-bottom: 30px;
}

.logo image {
	width: 120px;
	height: 120px;
}

.title {
	font-size: 24px;
	font-weight: bold;
	margin-bottom: 30px;
	color: #333;
}

.login-mode {
	display: flex;
	margin-bottom: 20px;
	border-radius: 8px;
	overflow: hidden;
	border: 1px solid #e0e0e0;
}

.mode-item {
	flex: 1;
	padding: 12px;
	text-align: center;
	background-color: #f5f5f5;
	transition: all 0.3s ease;
}

.mode-item.active {
	background-color: #007AFF;
	color: white;
}

.mode-item text {
	font-size: 16px;
}

.student-login {
	margin-bottom: 30px;
}

.student-login uni-easyinput {
	margin-bottom: 15px;
	width: 100%;
}

.school-list {
	width: 100%;
	margin-bottom: 40px;
}

.school-item {
	padding: 15px;
	border: 1px solid #e0e0e0;
	border-radius: 8px;
	margin-bottom: 10px;
	text-align: center;
	font-size: 16px;
	color: #333;
	transition: all 0.3s ease;
}

.school-item:hover {
	background-color: #f5f5f5;
}

.school-item.selected {
	background-color: #007AFF;
	color: white;
	border-color: #007AFF;
}

.login-btn {
	width: 100%;
	height: 45px;
	background-color: #007AFF;
	color: white;
	font-size: 16px;
	border-radius: 8px;
	margin-bottom: 20px;
}

.loading {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-top: 20px;
}

.loading text {
	margin-top: 10px;
	color: #666;
	font-size: 14px;
}
</style>