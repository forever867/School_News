<template>
	<view class="container">
		<!-- 顶部学校信息 -->
		<view class="school-header" v-if="school">
			<text class="school-name">{{ school.name }}</text>
			<text class="welcome-text">欢迎使用校园新闻</text>
		</view>
		
		<!-- 新闻公告区域 -->
		<view class="news-section">
			<view class="section-header">
				<text class="section-title">学校新闻</text>
				<text class="more" @click="loadMore('news')">更多</text>
			</view>
			
			<view class="news-list">
				<view class="news-item" v-for="(news, index) in newsList" :key="index" @click="viewNewsDetail(news)">
					<text class="news-title">{{ news.title }}</text>
					<text class="news-time">{{ news.time }}</text>
					<text class="news-brief">{{ news.brief }}</text>
				</view>
			</view>
		</view>
		
		<view class="news-section">
			<view class="section-header">
				<text class="section-title">学校公告</text>
				<text class="more" @click="loadMore('notice')">更多</text>
			</view>
			
			<view class="news-list">
				<view class="news-item" v-for="(notice, index) in noticeList" :key="index" @click="viewNewsDetail(notice)">
					<text class="news-title">{{ notice.title }}</text>
					<text class="news-time">{{ notice.time }}</text>
					<text class="news-brief">{{ notice.brief }}</text>
				</view>
			</view>
		</view>
		
		<!-- 功能入口 -->
		<view class="function-section">
			<view class="function-grid">
				<view class="function-item" @click="goToMap">
					<uni-icons type="location" size="32" color="#007AFF"></uni-icons>
					<text>学校地图</text>
				</view>
				<view class="function-item" @click="goToPost">
					<uni-icons type="compose" size="32" color="#007AFF"></uni-icons>
					<text>发布帖子</text>
				</view>
				<view class="function-item" @click="goToProfile">
					<uni-icons type="person" size="32" color="#007AFF"></uni-icons>
					<text>个人中心</text>
				</view>
				<view class="function-item" @click="refreshNews">
					<uni-icons type="refresh" size="32" color="#007AFF"></uni-icons>
					<text>刷新</text>
				</view>
			</view>
		</view>
		
		<!-- 未登录提示 -->
		<view class="login-tip" v-if="!school">
			<text>请先登录以查看校园新闻</text>
			<button class="login-btn" @click="goToLogin">去登录</button>
		</view>
		
		<!-- 加载中 -->
		<view class="loading" v-if="loading">
			<uni-loading type="spinner" color="#007AFF"></uni-loading>
			<text>正在加载新闻...</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			school: null,
			newsList: [],
			noticeList: [],
			loading: false
		}
	},
	onLoad() {
		this.checkLogin();
	},
	onShow() {
		this.checkLogin();
	},
	methods: {
			checkLogin() {
				const user = uni.getStorageSync('user');
				if (user) {
					if (user.type === 'student') {
						this.school = user.school;
					}
					this.loadNews();
				}
			},
		loadNews() {
			this.loading = true;
			
			// 模拟加载新闻和公告
			setTimeout(() => {
				this.newsList = [
					{
						title: '学校举办2026年春季运动会',
						time: '2026-04-01',
						brief: '4月1日，我校成功举办2026年春季运动会，来自各个学院的学生积极参与，展现了良好的体育精神和团队合作能力。',
						content: '4月1日，我校成功举办2026年春季运动会。本次运动会共有来自全校20个学院的3000余名学生参加，比赛项目包括田径、球类、趣味运动等多个类别。\n\n在开幕式上，各学院代表队以特色方阵入场，展示了各自的学院文化和精神风貌。校领导致开幕词，鼓励同学们积极参与体育锻炼，培养健康的生活方式。\n\n比赛过程中，运动员们奋勇拼搏，观众们热情助威，整个运动场充满了活力和激情。最终，计算机学院、物理学院和化学学院分别获得团体总分前三名。\n\n本次运动会不仅是一场体育盛会，更是一次展现我校学生精神风貌的机会，为校园文化建设增添了浓墨重彩的一笔。'
					},
					{
						title: '我校获得国家自然科学基金重点项目',
						time: '2026-03-28',
						brief: '近日，国家自然科学基金委员会公布了2026年度重点项目评审结果，我校申报的"新型量子材料的设计与制备"项目成功获批。',
						content: '近日，国家自然科学基金委员会公布了2026年度重点项目评审结果，我校物理学院张教授领衔的"新型量子材料的设计与制备"项目成功获批，获得直接经费支持800万元。\n\n该项目旨在通过理论计算与实验相结合的方法，设计并制备新型量子材料，探索其在量子计算、量子通信等领域的应用。项目团队由我校物理学院、化学学院和材料学院的多名专家组成，具有雄厚的研究实力。\n\n这一项目的获批，标志着我校在量子材料研究领域的学术水平得到了国家层面的认可，将进一步提升我校在相关领域的国际影响力。'
					},
					{
						title: '知名校友回校作学术报告',
						time: '2026-03-25',
						brief: '3月25日，我校知名校友、国际著名计算机科学家李教授回校作题为"人工智能前沿发展"的学术报告。',
						content: '3月25日，我校计算机学院1995届校友、国际著名计算机科学家李教授回到母校，为师生作了题为"人工智能前沿发展"的学术报告。\n\n李教授现任美国斯坦福大学计算机科学系主任，是人工智能领域的顶尖专家，在机器学习、计算机视觉等方面取得了丰硕的研究成果。报告中，他分享了人工智能领域的最新发展趋势，以及自己的研究心得和经验。\n\n报告结束后，李教授与现场师生进行了热烈的互动交流，回答了同学们关于学术研究、职业规划等方面的问题。他鼓励学弟学妹们要保持对科学的热爱和探索精神，勇攀学术高峰。'
					},
					{
						title: '学校开展消防安全教育活动',
						time: '2026-03-20',
						brief: '为增强师生消防安全意识，提高应急处置能力，我校于3月20日开展了消防安全教育活动。',
						content: '为增强师生消防安全意识，提高应急处置能力，我校于3月20日开展了消防安全教育活动。活动包括消防知识讲座、灭火器使用培训和火灾逃生演练等环节。\n\n在消防知识讲座中，市消防支队的专家详细讲解了火灾的预防措施、初期火灾的扑救方法以及火灾逃生的技巧。随后，师生们在消防队员的指导下，实际操作使用灭火器扑灭模拟火灾。\n\n最后，全体参与人员进行了火灾逃生演练，模拟了在火灾发生时的应急疏散过程。通过这次活动，师生们的消防安全意识得到了显著提高，掌握了基本的消防技能和逃生方法。'
					}
				];
				
				this.noticeList = [
					{
						title: '关于五一假期安排的通知',
						time: '2026-04-15',
						brief: '根据国务院办公厅通知，2026年五一劳动节放假安排如下：5月1日至5月5日放假调休，共5天。',
						content: '各单位、各部门：\n\n根据国务院办公厅通知，2026年五一劳动节放假安排如下：\n\n一、放假时间：5月1日至5月5日放假调休，共5天。4月28日（星期日）、5月11日（星期六）正常上班。\n\n二、有关要求：\n1. 各单位要做好假期期间的安全保卫工作，加强值班值守，确保校园安全。\n2. 学生处要做好学生假期管理，提醒学生注意假期安全。\n3. 后勤部门要做好假期期间的服务保障工作。\n4. 各单位要合理安排工作，确保假期前后各项工作正常开展。\n\n特此通知。\n\n学校办公室\n2026年4月15日'
					},
					{
						title: '2026年夏季学期选课通知',
						time: '2026-04-10',
						brief: '2026年夏季学期选课将于4月20日开始，请同学们提前做好准备，按照规定时间进行选课。',
						content: '全体学生：\n\n2026年夏季学期选课将于4月20日开始，具体安排如下：\n\n一、选课时间：\n1. 本科生：4月20日9:00至4月27日17:00\n2. 研究生：4月22日9:00至4月29日17:00\n\n二、选课流程：\n1. 登录教务系统，进入"选课管理"模块\n2. 浏览课程目录，选择心仪课程\n3. 确认选课结果\n\n三、注意事项：\n1. 请同学们根据自己的学习计划合理选课\n2. 选课期间可进行课程调整\n3. 选课结束后将关闭选课系统，不再接受任何形式的补选\n\n请同学们按时完成选课，如有疑问，请联系所在学院教学办公室。\n\n教务处\n2026年4月10日'
					},
					{
						title: '图书馆暑期开放时间调整',
						time: '2026-04-05',
						brief: '为满足暑期师生的学习需求，图书馆将于7月1日至8月31日调整开放时间。',
						content: '各位读者：\n\n为满足暑期师生的学习需求，图书馆将于2026年7月1日至8月31日调整开放时间，具体安排如下：\n\n一、开放时间：\n周一至周五：8:00-22:00\n周六至周日：9:00-21:00\n\n二、服务内容：\n1. 图书借还服务正常开展\n2. 电子资源24小时开放\n3. 自习室正常开放\n\n三、注意事项：\n1. 暑期期间，图书馆将定期进行系统维护，请关注图书馆网站通知\n2. 请读者自觉遵守图书馆规章制度，保持安静的学习环境\n\n如有疑问，请联系图书馆服务台：88888888\n\n图书馆\n2026年4月5日'
					}
				];
				
				this.loading = false;
			}, 1500);
		},
		loadMore(type) {
			uni.showToast({
				title: `加载更多${type === 'news' ? '新闻' : '公告'}功能开发中`,
				icon: 'none'
			});
		},
		viewNewsDetail(news) {
			// 存储新闻详情到本地，以便在详情页面显示
			uni.setStorageSync('newsDetail', news);
			// 跳转到新闻详情页面（实际应用中需要创建详情页面）
			uni.showModal({
				title: news.title,
				content: news.content,
				showCancel: false,
				confirmText: '确定'
			});
		},
		refreshNews() {
			this.loadNews();
		},
		goToLogin() {
			uni.navigateTo({
				url: '/pages/login/login'
			});
		},
		goToMap() {
			if (!this.school) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				return;
			}
			uni.navigateTo({
				url: '/pages/map/map'
			});
		},
		goToPost() {
				const user = uni.getStorageSync('user');
				if (!user) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				if (user.type === 'guest') {
					uni.showToast({
						title: '游客无法发帖，请使用学生账号登录',
						icon: 'none'
					});
					return;
				}
				uni.navigateTo({
					url: '/pages/post/post'
				});
			},
		goToProfile() {
			if (!this.school) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				return;
			}
			uni.navigateTo({
				url: '/pages/profile/profile'
			});
		}
	}
}
</script>

<style>
.container {
	padding: 0;
	min-height: 100vh;
}

.school-header {
	background-color: #007AFF;
	color: white;
	padding: 20px;
	text-align: center;
}

.school-name {
	display: block;
	font-size: 20px;
	font-weight: bold;
	margin-bottom: 5px;
}

.welcome-text {
	display: block;
	font-size: 14px;
	opacity: 0.9;
}

.news-section {
	background-color: white;
	margin: 10px;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.05);
	padding: 15px;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15px;
	padding-bottom: 10px;
	border-bottom: 1px solid #f0f0f0;
}

.section-title {
	font-size: 16px;
	font-weight: bold;
	color: #333;
}

.more {
	font-size: 14px;
	color: #007AFF;
}

.news-list {
	min-height: 100px;
}

.news-item {
	padding: 15px;
	border-bottom: 1px solid #f0f0f0;
	transition: all 0.3s ease;
}

.news-item:last-child {
	border-bottom: none;
}

.news-item:hover {
	background-color: #f5f5f5;
}

.news-title {
	display: block;
	font-size: 16px;
	font-weight: bold;
	color: #333;
	margin-bottom: 8px;
	line-height: 1.4;
}

.news-time {
	display: block;
	font-size: 12px;
	color: #999;
	margin-bottom: 8px;
}

.news-brief {
	display: block;
	font-size: 14px;
	color: #666;
	line-height: 1.4;
	overflow: hidden;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
}

.function-section {
	background-color: white;
	margin: 10px;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.05);
	padding: 20px;
}

.function-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 20px;
}

.function-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 5px;
}

.function-item text {
	font-size: 12px;
	color: #333;
}

.login-tip {
	margin: 40px 20px;
	padding: 30px;
	background-color: white;
	border-radius: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.05);
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 20px;
}

.login-tip text {
	font-size: 16px;
	color: #666;
}

.login-btn {
	padding: 8px 30px;
	background-color: #007AFF;
	color: white;
	font-size: 14px;
	border-radius: 20px;
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
