library(shiny)
library(tidyverse)


plot <- function(m){
  n1 <- data.frame(gen_data = rnorm(100, mean=10, sd=3), fn='N(10, 3)')
  mix <- data.frame(gen_data = rnorm(m, mean=20, sd=2), fn='N(20, 2)')
  combined <- rbind(n1, mix)
  c_mean <- mean(combined$gen_data)
  c_median <- median(combined$gen_data)
  lq <- quantile(combined$gen_data, 0.25)
  uq <- quantile(combined$gen_data, 0.75)
  
  p <- ggplot(combined, aes(x=gen_data)) +
    geom_histogram(binwidth = 1, fill='green', color='black', alpha=0.9) +
    geom_vline(xintercept = c_mean, size=1, color='black') + 
    geom_vline(xintercept = c_median, size=1, color='red') + 
    geom_vline(xintercept = lq, color='blue', size=1) + 
    geom_vline(xintercept = uq, color='blue', size=1) + 
    geom_label(x=c_mean, y=4, label='Mean', label.size = 0.01) +
    geom_label(x=c_median, y=8, label='Median', label.size = 0.01) + 
    geom_label(x=lq, y=12, label='0.25 Quantile') + 
    geom_label(x=uq, y=10, label='0.75 Quantile')
    
  return (p)
}
# Define UI for slider demo app ----
ui <- fluidPage(
  
  # App title ----
  titlePanel("Sliders"),
  
  # Sidebar layout with input and output definitions ----
  sidebarLayout(
    
    # Sidebar to demonstrate various slider options ----
    sidebarPanel(
      # Input: Simple integer interval ----
      sliderInput("m", "Sample size(m):",
                  min = 10, max = 200,
                  value = 50)),
    
    # Main panel for displaying outputs ----
    mainPanel(
      # Output: Table summarizing the values entered ----
      plotOutput("plot1", height = 350,
                 # Equivalent to: click = clickOpts(id = "plot_click")
                 click = "plot_click",
                 dblclick = dblclickOpts(
                   id = "plot_dblclick"
                 ),
                 hover = hoverOpts(
                   id = "plot_hover"
                 ),
                 brush = brushOpts(
                   id = "plot_brush"
                 )
      )
    
    )
  )
)

# Define server logic for slider examples ----
server <- function(input, output) {
  
  # Reactive expression to create data frame of all input values ----
  sliderValues <- reactive({
    
    data.frame(
      Name = c("Sample size (m)"),
      Value = as.character(c(input$m)),
      stringsAsFactors = FALSE)
    
  })
  
  # Show the values in an HTML table ----
  output$values <- renderTable({
    sliderValues()
  })
  
  output$plot1 <- renderPlot({
     plot(input$m)
  })
  
}
# Run the app ----
shinyApp(ui = ui, server = server)

