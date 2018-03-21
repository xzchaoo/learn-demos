<#macro getValueExpression transferType priceType>
	<#if transferType=='DIRECT'>
		<#if priceType=='TOTAL_PRICE'>this.directTotalPrice.total<#else>this.directSalesPrice.price</#if>
	<#else>
		<#if priceType=='TOTAL_PRICE'>this.totalPrice.total<#else>this.salesPrice.price</#if>
	</#if>
</#macro>
<#macro getPriceExpression transferType priceType>
	<#if transferType=='DIRECT'>
		<#if priceType=='TOTAL_PRICE'>this.directTotalPrice<#else>this.directSalesPrice</#if>
	<#else>
		<#if priceType=='TOTAL_PRICE'>this.totalPrice<#else>this.salesPrice</#if>
	</#if>
</#macro>

function map()
<#--直飞价格不存在的检查-->
<#if transferType=='DIRECT'>
	if(!this.directTotalPrice){
		return
	}
</#if>

	var key = <#if groupByCityPair>this.departureCityCode + this.arrivalCityCode</#if><#if groupByDates><#if groupByCityPair> + </#if>this.outboundDate<#if tripType=='RT'> + this.inboundDate</#if></#if>
	emit(key, {
		value: <@getValueExpression transferType priceType/>
		entity: {
			departureCityCode: this.departureCityCode,
			arrivalCityCode: this.arrivalCityCode,
			outboundDate: this.outboundDate,
<#if tripType=='RT'>
			inboundDate: this.inboundDate,
</#if>
			price: <@getPriceExpression transferType priceType/>,
		}
	})
}