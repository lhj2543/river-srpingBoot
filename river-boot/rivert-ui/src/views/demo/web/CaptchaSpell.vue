<style>

    .block {
        position: absolute;
        left: 0;
        top: 0;
    }
        
    .sliderContainer {
        position: relative;
        text-align: center;
        width: 310px;
        height: 40px;
        line-height: 40px;
        background: #f7f9fa;
        color: #45494c;
        border: 1px solid #e4e7eb;
    }
        
    .sliderContainer_active .slider {
        height: 38px;
        top: -1px;
        border: 1px solid #1991FA;
    }
        
    .sliderContainer_active .sliderMask {
        height: 38px;
        border-width: 1px;
    }
        
    .sliderContainer_success .slider {
        height: 38px;
        top: -1px;
        border: 1px solid #52CCBA;
        background-color: #52CCBA !important;
    }
        
    .sliderContainer_success .sliderMask {
        height: 38px;
        border: 1px solid #52CCBA;
        background-color: #D2F4EF;
    }
        
    .sliderContainer_success .sliderIcon {
        background-position: 0 0 !important;
    }
        
    .sliderContainer_fail .slider {
        height: 38px;
        top: -1px;
        border: 1px solid #f57a7a;
        background-color: #f57a7a !important;
    }
        
    .sliderContainer_fail .sliderMask {
        height: 38px;
        border: 1px solid #f57a7a;
        background-color: #fce1e1;
    }
        
    .sliderContainer_fail .sliderIcon {
        top: 14px;
        background-position: 0 -82px !important;
    }
    .sliderContainer_active .sliderText, .sliderContainer_success .sliderText, .sliderContainer_fail .sliderText {
        display: none;
    }
        
    .sliderMask {
        position: absolute;
        left: 0;
        top: 0;
        height: 40px;
        border: 0 solid #1991FA;
        background: #D1E9FE;
    }
        
    .slider {
        position: absolute;
        top: 0;
        left: 0;
        width: 40px;
        height: 40px;
        background: #fff;
        box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
        cursor: pointer;
        transition: background .2s linear;
    }
        
    .slider:hover {
        background: #1991FA;
    }
        
    .slider:hover .sliderIcon {
        background-position: 0 -13px;
    }
        
    .sliderIcon {
        position: absolute;
        top: 15px;
        left: 13px;
        width: 14px;
        height: 12px;
        background: url(/static/image/captcha/icon_light.f13cff3.png) 0 -26px;
        background-size: 34px 471px;
    }
        
    .refreshIcon {
        position: absolute;
        right: 0;
        top: 0;
        width: 34px;
        height: 34px;
        cursor: pointer;
        background: url(/static/image/captcha/icon_light.f13cff3.png) 0 -437px;
        background-size: 34px 471px;
        z-index:99;
    }

    .river-captcha-spell{
        width:310px;margin:0 auto;padding:50px 0;
    }
</style>

<template>

  <!-- 滑动验证码 -->
  <div class="river-captcha-spell">
    <div id="captcha"></div>
  </div>

</template>
<script>

(function (window) {
    const l = 42, // 滑块边长
    r = 9, // 滑块半径
    w = 310, // canvas宽度
    h = 155, // canvas高度
    PI = Math.PI
    const L = l + r * 2 + 3 // 滑块实际边长
    
    function getRandomNumberByRange (start, end) {
        return Math.round(Math.random() * (end - start) + start)
    }
    
    function createCanvas (width, height) {
    const canvas = createElement('canvas')
    canvas.width = width
    canvas.height = height
    return canvas
    }
    
    function createImg (onload) {
    const img = createElement('img')
    img.crossOrigin = "Anonymous"
    img.onload = onload
    img.onerror = () => {
        img.src = getRandomImg()
    }
    img.src = getRandomImg()
        return img
    }
    
    function createElement (tagName, className) {
        const elment = document.createElement(tagName)
        elment.className = className
        return elment
    }
    
    function addClass (tag, className) {
        tag.classList.add(className)
    }
    
    function removeClass (tag, className) {
        tag.classList.remove(className)
    }
    
    function getRandomImg () {
        return '/static/image/captcha/' + getRandomNumberByRange(0, 10)+'.jpg'
    }
    
    function draw (ctx, x, y, operation) {
        ctx.beginPath()
        ctx.moveTo(x, y)
        ctx.arc(x + l / 2, y - r + 2, r, 0.72 * PI, 2.26 * PI)
        ctx.lineTo(x + l, y)
        ctx.arc(x + l + r - 2, y + l / 2, r, 1.21 * PI, 2.78 * PI)
        ctx.lineTo(x + l, y + l)
        ctx.lineTo(x, y + l)
        ctx.arc(x + r - 2, y + l / 2, r + 0.4, 2.76 * PI, 1.24 * PI, true)
        ctx.lineTo(x, y)
        ctx.lineWidth = 2
        ctx.fillStyle = 'rgba(255, 255, 255, 0.7)'
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.7)'
        ctx.stroke()
        ctx[operation]()
        ctx.globalCompositeOperation = 'overlay'
    }
    
    function sum (x, y) {
        return x + y
    }
    
    function square (x) {
        return x * x
    }
    
    class jigsaw {
    constructor ({ el, onSuccess, onFail, onRefresh }) {
        el.style.position = el.style.position || 'relative'
        this.el = el
        this.onSuccess = onSuccess
        this.onFail = onFail
        this.onRefresh = onRefresh
    }
    
    init () {
        this.initDOM()
        this.initImg()
        this.bindEvents()
    }
    
    initDOM () {
        const canvas = createCanvas(w, h) // 画布
        const block = canvas.cloneNode(true) // 滑块
        const sliderContainer = createElement('div', 'sliderContainer')
        const refreshIcon = createElement('div', 'refreshIcon')
        const sliderMask = createElement('div', 'sliderMask')
        const slider = createElement('div', 'slider')
        const sliderIcon = createElement('span', 'sliderIcon')
        const text = createElement('span', 'sliderText')
    
        block.className = 'block'
        text.innerHTML = '向右滑动填充拼图'
    
        const el = this.el
        el.appendChild(canvas)
        el.appendChild(refreshIcon)
        el.appendChild(block)
        slider.appendChild(sliderIcon)
        sliderMask.appendChild(slider)
        sliderContainer.appendChild(sliderMask)
        sliderContainer.appendChild(text)
        el.appendChild(sliderContainer)
    
        Object.assign(this, {
        canvas,
        block,
        sliderContainer,
        refreshIcon,
        slider,
        sliderMask,
        sliderIcon,
        text,
        canvasCtx: canvas.getContext('2d'),
        blockCtx: block.getContext('2d')
        })
    }
    
    initImg () {
        const img = createImg(() => {
        this.draw()
        this.canvasCtx.drawImage(img, 0, 0, w, h)
        this.blockCtx.drawImage(img, 0, 0, w, h)
        const y = this.y - r * 2 - 1
        const ImageData = this.blockCtx.getImageData(this.x - 3, y, L, L)
        this.block.width = L
        this.blockCtx.putImageData(ImageData, 0, y)
        })
        this.img = img
    }
    
    draw () {
        // 随机创建滑块的位置
        this.x = getRandomNumberByRange(L + 10, w - (L + 10))
        this.y = getRandomNumberByRange(10 + r * 2, h - (L + 10))
        draw(this.canvasCtx, this.x, this.y, 'fill')
        draw(this.blockCtx, this.x, this.y, 'clip')
    }
    
    clean () {
        this.canvasCtx.clearRect(0, 0, w, h)
        this.blockCtx.clearRect(0, 0, w, h)
        this.block.width = w
    }
    
    bindEvents () {
        this.el.onselectstart = () => false
        this.refreshIcon.onclick = () => {
        this.reset()
        typeof this.onRefresh === 'function' && this.onRefresh()
        }
    
        let originX, originY, trail = [], isMouseDown = false
    
        const handleDragStart = function (e) {
        originX = e.clientX || e.touches[0].clientX
        originY = e.clientY || e.touches[0].clientY
        isMouseDown = true
        }
    
        const handleDragMove = (e) => {
        if (!isMouseDown) return false
        const eventX = e.clientX || e.touches[0].clientX
        const eventY = e.clientY || e.touches[0].clientY
        const moveX = eventX - originX
        const moveY = eventY - originY
        if (moveX < 0 || moveX + 38 >= w) return false
        this.slider.style.left = moveX + 'px'
        const blockLeft = (w - 40 - 20) / (w - 40) * moveX
        this.block.style.left = blockLeft + 'px'
    
        addClass(this.sliderContainer, 'sliderContainer_active')
        this.sliderMask.style.width = moveX + 'px'
        trail.push(moveY)
        }
    
        const handleDragEnd = (e) => {
        if (!isMouseDown) return false
        isMouseDown = false
        const eventX = e.clientX || e.changedTouches[0].clientX
        if (eventX == originX) return false
        removeClass(this.sliderContainer, 'sliderContainer_active')
        this.trail = trail
        const { spliced, verified } = this.verify()
        if (spliced) {
            if (verified) {
            addClass(this.sliderContainer, 'sliderContainer_success')
            typeof this.onSuccess === 'function' && this.onSuccess()
            } else {
            addClass(this.sliderContainer, 'sliderContainer_fail')
            this.text.innerHTML = '再试一次'
            this.reset()
            }
        } else {
            addClass(this.sliderContainer, 'sliderContainer_fail')
            typeof this.onFail === 'function' && this.onFail()
            setTimeout(() => {
            this.reset()
            }, 1000)
        }
        }
        this.slider.addEventListener('mousedown', handleDragStart)
        this.slider.addEventListener('touchstart', handleDragStart)
        document.addEventListener('mousemove', handleDragMove)
        document.addEventListener('touchmove', handleDragMove)
        document.addEventListener('mouseup', handleDragEnd)
        document.addEventListener('touchend', handleDragEnd)
    }
    
    verify () {
        const arr = this.trail // 拖动时y轴的移动距离
        const average = arr.reduce(sum) / arr.length
        const deviations = arr.map(x => x - average)
        const stddev = Math.sqrt(deviations.map(square).reduce(sum) / arr.length)
        const left = parseInt(this.block.style.left)
        return {
        spliced: Math.abs(left - this.x) < 10,
        verified: stddev !== 0, // 简单验证下拖动轨迹，为零时表示Y轴上下没有波动，可能非人为操作
        }
    }
    
    reset () {
        this.sliderContainer.className = 'sliderContainer'
        this.slider.style.left = 0
        this.block.style.left = 0
        this.sliderMask.style.width = 0
        this.clean()
        this.img.src = getRandomImg()
    }
    
    }
    
    window.jigsaw = {
    init: function (opts) {
        return new jigsaw(opts).init()
    }
    }
}(window))


export default {
  name: 'captchaSpell',
  data(){
    return {
    }
  },
  created(){

  },
  mounted(){
     jigsaw.init({
        el: document.getElementById('captcha'),
    })
  },
  methods: {

  },
  watch: {//数据监听
  
  },
  beforeDestroy(){
        
  }

}
</script>
