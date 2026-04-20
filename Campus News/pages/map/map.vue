<template>
	<view class="container">
		<view class="map-container">
			<view class="map" id="map">
				<!-- 模拟地图显示 -->
				<view class="map-placeholder">
					<uni-icons type="map" size="64" color="#007AFF"></uni-icons>
					<text>地图加载中...</text>
					<view class="map-markers" v-if="!loading">
						<view class="marker school-marker" v-if="schoolLocation">
							<uni-icons type="location-filled" size="32" color="#FF3B30"></uni-icons>
							<text class="marker-label">{{ school.name }}</text>
						</view>
						<view class="marker user-marker" v-if="currentLocation">
							<uni-icons type="person" size="24" color="#007AFF"></uni-icons>
							<text class="marker-label">当前位置</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="school-info" v-if="school">
			<text class="school-name">{{ school.name }}</text>
			<text class="school-address">{{ school.address }}</text>
		</view>
		
		<view class="location-btn" @click="getLocation">
			<uni-icons type="location" size="24" color="#007AFF"></uni-icons>
			<text>定位当前位置</text>
		</view>
		
		<view class="map-controls">
			<view class="control-item" @click="zoomIn">
				<uni-icons type="plus" size="20" color="#333"></uni-icons>
			</view>
			<view class="control-item" @click="zoomOut">
				<uni-icons type="minus" size="20" color="#333"></uni-icons>
			</view>
		</view>
		
		<view class="loading" v-if="loading">
			<uni-loading type="spinner" color="#007AFF"></uni-loading>
			<text>正在加载地图...</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			school: null,
			loading: true,
			mapContext: null,
			currentLocation: null,
			schoolLocation: null,
			zoom: 15
		}
	},
	onLoad() {
		const user = uni.getStorageSync('user');
		if (!user || user.type !== 'student') {
			uni.showToast({
				title: '请使用学生账号登录以查看学校地图',
				icon: 'none'
			});
			uni.navigateTo({
				url: '/pages/login/login'
			});
			return;
		}
		this.school = user.school;
		
		// 模拟学校地址和坐标数据
		const schoolData = {
			'北京大学': {
				address: '北京市海淀区颐和园路5号',
				latitude: 39.9928,
				longitude: 116.3055
			},
			'清华大学': {
				address: '北京市海淀区清华园1号',
				latitude: 39.9983,
				longitude: 116.3289
			},
			'复旦大学': {
				address: '上海市杨浦区邯郸路220号',
				latitude: 31.2985,
				longitude: 121.5361
			},
			'上海交通大学': {
				address: '上海市闵行区东川路800号',
				latitude: 31.0293,
				longitude: 121.4361
			},
			'浙江大学': {
				address: '浙江省杭州市西湖区余杭塘路866号',
				latitude: 30.3086,
				longitude: 120.0934
			},
			'青岛科技大学': {
				address: '山东省青岛市崂山区松岭路99号',
				latitude: 36.1997,
				longitude: 120.4176
			}
		};
		
		const schoolInfo = schoolData[this.school.name] || { address: '未知地址', latitude: 0, longitude: 0 };
		this.school.address = schoolInfo.address;
		this.schoolLocation = {
			latitude: schoolInfo.latitude,
			longitude: schoolInfo.longitude
		};
		
		// 初始化地图
		this.initMap();
	},
	methods: {
		initMap() {
			// 模拟地图加载
			setTimeout(() => {
				this.loading = false;
				// 在实际应用中，这里会调用地图API初始化地图
				console.log('地图初始化完成');
			}, 1500);
		},
		getLocation() {
			uni.showToast({
				title: '正在定位...',
				icon: 'none'
			});
			
			// 模拟获取当前位置
			setTimeout(() => {
				// 根据学校位置生成附近的模拟位置
				let latitude = this.schoolLocation.latitude + (Math.random() - 0.5) * 0.01;
				let longitude = this.schoolLocation.longitude + (Math.random() - 0.5) * 0.01;
				
				this.currentLocation = {
					latitude: latitude,
					longitude: longitude,
					address: '学校附近'
				};
				uni.showToast({
					title: '定位成功',
					icon: 'success'
				});
				// 在实际应用中，这里会更新地图显示当前位置
				console.log('定位结果:', this.currentLocation);
			}, 1000);
		},
		zoomIn() {
			if (this.zoom < 20) {
				this.zoom += 1;
				uni.showToast({
					title: '放大地图',
					icon: 'none'
				});
			}
		},
		zoomOut() {
			if (this.zoom > 5) {
				this.zoom -= 1;
				uni.showToast({
					title: '缩小地图',
					icon: 'none'
				});
			}
		}
	}
}
</script>

<style>
.container {
	padding: 0;
	min-height: 100vh;
}

.map-container {
	width: 100%;
	height: 60vh;
	background-color: #f0f0f0;
}

.map {
	width: 100%;
	height: 100%;
	background-color: #e0e0e0;
	position: relative;
}

.map-placeholder {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
}

.map-placeholder text {
	margin-top: 10px;
	color: #666;
	font-size: 14px;
}

.map-markers {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	pointer-events: none;
}

.marker {
	position: absolute;
	transform: translate(-50%, -100%);
	display: flex;
	flex-direction: column;
	align-items: center;
	pointer-events: auto;
}

.school-marker {
	top: 50%;
	left: 50%;
}

.user-marker {
	top: 60%;
	left: 60%;
}

.marker-label {
	background-color: rgba(0,0,0,0.7);
	color: white;
	padding: 4px 8px;
	border-radius: 4px;
	font-size: 12px;
	margin-top: 5px;
	white-space: nowrap;
}

.school-info {
	padding: 15px;
	background-color: white;
	border-bottom: 1px solid #e0e0e0;
}

.school-name {
	display: block;
	font-size: 18px;
	font-weight: bold;
	color: #333;
	margin-bottom: 5px;
}

.school-address {
	display: block;
	font-size: 14px;
	color: #666;
}

.location-btn {
	position: fixed;
	bottom: 30px;
	right: 20px;
	background-color: white;
	padding: 10px 15px;
	border-radius: 25px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.1);
	display: flex;
	align-items: center;
	gap: 8px;
	z-index: 10;
}

.location-btn text {
	font-size: 14px;
	color: #007AFF;
}

.map-controls {
	position: fixed;
	bottom: 100px;
	right: 20px;
	display: flex;
	flex-direction: column;
	gap: 10px;
	z-index: 10;
}

.control-item {
	width: 40px;
	height: 40px;
	background-color: white;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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
	z-index: 20;
}

.loading text {
	margin-top: 10px;
	color: #666;
	font-size: 14px;
}
</style>